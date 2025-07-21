package com.jmr.medhealth.data.repository

import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * A repository for managing and retrieving configuration values from Firebase Remote Config.
 * This class is provided as a singleton by Hilt to ensure a single source of truth for configuration.
 */
@Singleton
class RemoteConfigRepository @Inject constructor() {

    // Get the Firebase Remote Config instance using the KTX extensions.
    private val remoteConfig = Firebase.remoteConfig

    init {
        // Configure the settings for Remote Config.
        val configSettings = remoteConfigSettings {
            // Set a minimum fetch interval to prevent throttling during development.
            // For production, a value like 3600 (1 hour) is recommended.
            minimumFetchIntervalInSeconds = 3600
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        // Set the default values from our local constants.
        // This is crucial for the app to function correctly on first launch
        // or when the device is offline and cannot fetch new values.
        remoteConfig.setDefaultsAsync(mapOf(
            AI_AGENT_PROMPT_KEY to DEFAULT_AI_AGENT_PROMPT
        ))
    } // <-- THIS BRACE WAS LIKELY THE ONE MISSING

    /**
     * Asynchronously fetches the latest configuration values from the Firebase backend
     * and activates them, making them available to the app.
     * This should be called once on app startup.
     */
    suspend fun fetchAndActivate() {
        try {
            val success = remoteConfig.fetchAndActivate().await()
            if (success) {
                Log.d(TAG, "Remote Config parameters activated successfully.")
            } else {
                Log.d(TAG, "Remote Config fetch failed or no new values to activate.")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch and activate Remote Config", e)
        }
    }

    /**
     * Retrieves the current value for the AI Health Agent's system prompt.
     * It will return the fetched value from the server if available, otherwise it
     * falls back to the hard-coded default.
     */
    fun getAiAgentPrompt(): String {
        return remoteConfig.getString(AI_AGENT_PROMPT_KEY)
    }

    /**
     * Companion object to hold constants, preventing "magic strings" in the code.
     */
    companion object {
        private const val TAG = "RemoteConfigRepo"

        // The key used in the Firebase Remote Config console.
        private const val AI_AGENT_PROMPT_KEY = "ai_agent_prompt"

        // The default system prompt for the AI Health Agent. This is used if the app
        // can't fetch a value from the server. It includes the persona, knowledge context,
        // and the mandatory disclaimer. The '%s' is a placeholder for the user's question.
        private const val DEFAULT_AI_AGENT_PROMPT = """
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

            User's question: "%s"
        """
    }
}