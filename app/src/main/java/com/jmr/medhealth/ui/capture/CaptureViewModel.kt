package com.jmr.medhealth.ui.capture

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.jmr.medhealth.data.repository.DiagnosisRepository
import com.jmr.medhealth.util.TFLiteHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Represents the different states of the capture and analysis screen.
 */
sealed class CaptureUiState {
    object Idle : CaptureUiState()
    object Loading : CaptureUiState()
    data class Success(val diagnosisId: Int) : CaptureUiState()
    data class Error(val message: String) : CaptureUiState()
}

/**
 * ViewModel for the CaptureScreen. It handles the logic for image analysis,
 * saving the diagnosis, and managing the UI state.
 *
 * @param diagnosisRepository Repository for accessing diagnosis data sources.
 * @param analytics Firebase Analytics instance for logging custom events.
 */
@HiltViewModel
class CaptureViewModel @Inject constructor(
    private val diagnosisRepository: DiagnosisRepository,
    private val analytics: FirebaseAnalytics
) : ViewModel() {

    private val _uiState = MutableStateFlow<CaptureUiState>(CaptureUiState.Idle)
    val uiState: StateFlow<CaptureUiState> = _uiState.asStateFlow()

    /**
     * Analyzes the provided bitmap using the TFLite helper, logs the result to
     * Firebase Analytics, and saves the complete diagnosis via the repository.
     */
    fun analyzeImage(bitmap: Bitmap, classifier: TFLiteHelper) {
        viewModelScope.launch {
            _uiState.value = CaptureUiState.Loading
            try {
                // Perform classification on a background thread
                val results = classifier.classifyImage(bitmap)
                classifier.close() // Important: Release TFLite resources

                if (results.isNotEmpty()) {
                    val topResult = results.first()

                    // Log a custom event to Firebase Analytics
                    analytics.logEvent("diagnosis_performed") {
                        param("ai_result_label", topResult.first)
                        param("ai_result_confidence", topResult.second.toDouble())
                    }

                    // Save the diagnosis to the repository (which handles Room and Firestore)
                    val saveResult = diagnosisRepository.saveDiagnosis(
                        bitmap = bitmap,
                        result = topResult.first,
                        confidence = topResult.second
                    )

                    // Update the UI based on the result of the save operation
                    saveResult.fold(
                        onSuccess = { newId -> _uiState.value = CaptureUiState.Success(newId) },
                        onFailure = { error -> _uiState.value = CaptureUiState.Error(error.message ?: "Failed to save result") }
                    )
                } else {
                    _uiState.value = CaptureUiState.Error("Could not classify the image.")
                }
            } catch (e: Exception) {
                _uiState.value = CaptureUiState.Error(e.message ?: "An unexpected error occurred during analysis.")
            }
        }
    }

    /**
     * Resets the UI state back to Idle, ready for a new capture.
     * This should be called by the UI after handling a Success or Error state.
     */
    fun resetState() {
        _uiState.value = CaptureUiState.Idle
    }
}