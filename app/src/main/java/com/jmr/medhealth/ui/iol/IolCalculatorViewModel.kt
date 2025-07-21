package com.jmr.medhealth.ui.iol

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.math.pow

data class IolUiState(
    val axialLength: String = "",
    val k1: String = "",
    val k2: String = "",
    val aConstant: String = "",
    val calculatedIolPower: Double? = null,
    val errorMessage: String? = null
)

class IolCalculatorViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(IolUiState())
    val uiState = _uiState.asStateFlow()

    fun onAxialLengthChanged(value: String) {
        _uiState.value = _uiState.value.copy(axialLength = value, calculatedIolPower = null, errorMessage = null)
    }
    fun onK1Changed(value: String) {
        _uiState.value = _uiState.value.copy(k1 = value, calculatedIolPower = null, errorMessage = null)
    }
    fun onK2Changed(value: String) {
        _uiState.value = _uiState.value.copy(k2 = value, calculatedIolPower = null, errorMessage = null)
    }
    fun onAConstantChanged(value: String) {
        _uiState.value = _uiState.value.copy(aConstant = value, calculatedIolPower = null, errorMessage = null)
    }

    // This is a simplified implementation of the SRK/T formula.
    fun calculate() {
        val al = _uiState.value.axialLength.toDoubleOrNull()
        val k1 = _uiState.value.k1.toDoubleOrNull()
        val k2 = _uiState.value.k2.toDoubleOrNull()
        val a = _uiState.value.aConstant.toDoubleOrNull()

        if (al == null || k1 == null || k2 == null || a == null) {
            _uiState.value = _uiState.value.copy(errorMessage = "All fields must be valid numbers.")
            return
        }

        // --- SRK/T Formula Logic ---
        val avgK = (k1 + k2) / 2
        val r = 337.5 / avgK // Corneal radius

        var alc = al
        if (al > 24.2) {
            // Simplified axial length correction for long eyes
            alc = -3.446 + 1.716 * al - 0.02267 * al.pow(2)
        }

        val p = (1336 / (alc - 0.05)) - (1336 / ((1336 / (a - 0.05 - (0.65696 * r - 68.747))) - r))

        _uiState.value = _uiState.value.copy(calculatedIolPower = p, errorMessage = null)
    }
}