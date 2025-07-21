package com.jmr.medhealth.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.jmr.medhealth.BuildConfig
import javax.inject.Inject
import javax.inject.Singleton

data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)

@Singleton
class ChatRepository @Inject constructor(
    private val remoteConfig: RemoteConfigRepository
) {

    private val generativeModel = GenerativeModel(
        modelName = "gemini-1.5-flash",
        apiKey = BuildConfig.GOOGLE_API_KEY
    )

    suspend fun sendMessage(prompt: String): Result<String> {
        return try {
            val promptTemplate = remoteConfig.getAiAgentPrompt()
            val fullPrompt = String.format(promptTemplate, prompt)

            val response = generativeModel.generateContent(fullPrompt)
            Result.success(response.text ?: "I'm sorry, I couldn't generate a response.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
