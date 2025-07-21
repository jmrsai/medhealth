package com.jmr.medhealth.data.repository

import com.google.ai.client.generativeai.GenerativeModel
import com.jmr.medhealth.BuildConfig
import javax.inject.Inject // <-- ADD THIS IMPORT
import javax.inject.Singleton // <-- ADD THIS IMPORT for good practice

// ChatMessage data class remains the same
data class ChatMessage(
    val text: String,
    val isFromUser: Boolean
)

// The repository for the chat feature
@Singleton // Mark as a singleton so only one instance is created
class ChatRepository @Inject constructor() { // <-- ADDED @Inject constructor()

    
    suspend fun sendMessage(prompt: String): Result<String> {
        return try {
            // --- NEW, ADVANCED PROMPT ---
            // We are giving the AI a new persona and contextual knowledge.
            val fullPrompt = """
                You are a highly knowledgeable AI health and medical research assistant. 
                Your purpose is to provide clear, accurate information on both general health topics and advanced medical AI research.
                
                You must ALWAYS include the following disclaimer at the end of every response:
                'Disclaimer: I am an AI assistant, not a medical professional. This information is for educational purposes only. Always consult a qualified healthcare provider for medical advice.'

                --- CONTEXT FROM MEDICAL AI RESEARCH ---
                You are aware of the following state-of-the-art models and concepts in medical cross-modal pre-training (MCP). When asked about them, use this information to form your answer:
                - Med-PaLM & Med-PaLM M: Large multimodal generative models by Google, capable of synthesizing information from various medical data types including text, imaging, and genomics.
                - GatorTron: A large language model developed at the University of Florida for clinical text processing.
                - BioGPT: A generative pre-trained transformer for biomedical text generation and mining.
                - Clinical-BERT: A BERT-based model pre-trained on clinical notes (MIMIC-III dataset) for tasks like patient risk stratification.
                - Cross-modal Pre-training (MCP): A technique to train AI models on multiple data types simultaneously (e.g., chest X-rays and their corresponding radiologist reports) to learn richer, more useful representations of medical data.
                - Key Datasets: ROCO (Radiology Objects in COntext), MIMIC-CXR (Chest X-ray dataset with reports).
                -----------------------------------------

                Based on your persona and the context above, answer the user's question.

                User's question: "$prompt"
            """.trimIndent()

            val response = generativeModel.generateContent(fullPrompt)
            Result.success(response.text ?: "I'm sorry, I couldn't generate a response.")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}