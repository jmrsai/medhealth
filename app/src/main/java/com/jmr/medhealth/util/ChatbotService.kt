package com.jmr.medhealth.util

import com.jmr.medhealth.data.model.ChatbotMessage
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A singleton service responsible for handling communication with the Gemini API.
 */
@Singleton
class ChatbotService @Inject constructor() {

    // The API key for accessing the Gemini API.
    // The actual key will be provided by the runtime environment.
    private val apiKey = "AIzaSyDInPaySWNz1piQm6fFZ4Nu9kPqTd1iOBk"
    private val apiUrl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash-preview-05-20:generateContent?key=$apiKey"

    private val gson = Gson()

    /**
     * Data class representing a single part of the content.
     */
    private data class Part(@SerializedName("text") val text: String)

    /**
     * Data class representing a content block in the API request.
     */
    private data class Content(
        @SerializedName("role") val role: String,
        @SerializedName("parts") val parts: List<Part>
    )

    /**
     * Data class for the API request payload.
     */
    private data class RequestPayload(@SerializedName("contents") val contents: List<Content>)

    /**
     * Data class for the API response.
     */
    private data class ApiResponse(
        @SerializedName("candidates") val candidates: List<Candidate>
    )

    /**
     * Data class for an API response candidate.
     */
    private data class Candidate(@SerializedName("content") val content: Content)

    /**
     * Gets a response from the Gemini API based on the chat history.
     * @param systemPrompt A prompt to guide the AI's behavior.
     * @param chatHistory The list of ChatbotMessage objects representing the conversation.
     * @return The text response from the AI.
     */
    suspend fun getChatbotResponse(systemPrompt: String, chatHistory: List<ChatbotMessage>): String {
        return withContext(Dispatchers.IO) {
            val historyWithSystemPrompt = mutableListOf<ChatbotMessage>().apply {
                // Add the system prompt as an initial "model" message.
                // This is a common pattern to set the context for the model.
                add(ChatbotMessage(text = systemPrompt, isUser = false))
                // Add the rest of the conversation history.
                addAll(chatHistory)
            }

            // Map the ChatbotMessage list to the API's content format.
            val contents = historyWithSystemPrompt.map {
                Content(
                    role = if (it.isUser) "user" else "model",
                    parts = listOf(Part(it.text))
                )
            }

            // Build the request payload.
            val payload = RequestPayload(contents = contents)
            val jsonPayload = gson.toJson(payload)

            val url = URL(apiUrl)
            val connection = url.openConnection() as HttpURLConnection
            try {
                // Configure the HTTP request.
                connection.requestMethod = "POST"
                connection.setRequestProperty("Content-Type", "application/json")
                connection.doOutput = true
                connection.outputStream.bufferedWriter().use { writer ->
                    writer.write(jsonPayload)
                }

                // Check the response code.
                val responseCode = connection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read the response from the input stream.
                    val reader = BufferedReader(InputStreamReader(connection.inputStream))
                    val response = reader.readText()
                    reader.close()

                    // Parse the JSON response.
                    val apiResponse = gson.fromJson(response, ApiResponse::class.java)
                    // Extract the text from the first candidate.
                    return@withContext apiResponse.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text ?: "No response from AI."
                } else {
                    val errorReader = BufferedReader(InputStreamReader(connection.errorStream))
                    val errorResponse = errorReader.readText()
                    errorReader.close()
                    throw Exception("API call failed with code $responseCode: $errorResponse")
                }
            } finally {
                connection.disconnect()
            }
        }
    }
}
