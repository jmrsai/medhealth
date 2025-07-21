package com.jmr.medhealth.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.medhealth.data.repository.DiagnosisRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: DiagnosisRepository
) : ViewModel() {

    val diagnosisHistory = repository.getDiagnosisHistory()

    fun syncHistory() {
        viewModelScope.launch {
            repository.syncHistory()
        }
    }
}