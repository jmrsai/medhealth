package com.jmr.medhealth.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.medhealth.data.local.Diagnosis
import com.jmr.medhealth.data.repository.DiagnosisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class DetailsUiState {
    object Loading : DetailsUiState()
    data class Success(val diagnosis: Diagnosis) : DetailsUiState()
    data class Error(val message: String) : DetailsUiState()
}

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val repository: DiagnosisRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<DetailsUiState>(DetailsUiState.Loading)
    val uiState: StateFlow<DetailsUiState> = _uiState

    fun loadDiagnosis(id: Int) {
        viewModelScope.launch {
            _uiState.value = DetailsUiState.Loading
            val result = repository.getDiagnosisById(id)
            _uiState.value = if (result != null) {
                DetailsUiState.Success(result)
            } else {
                DetailsUiState.Error("Could not find the requested diagnosis.")
            }
        }
    }

    fun saveNotes(diagnosisId: Int, notes: String) {
        viewModelScope.launch {
            repository.updateDiagnosisNotes(diagnosisId, notes)
            // Refresh data after saving
            loadDiagnosis(diagnosisId)
        }
    }
}