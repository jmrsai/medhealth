package com.jmr.medhealth.ui.iol

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun IolCalculatorScreen(viewModel: IolCalculatorViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("IOL Power Calculator (SRK/T)", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.axialLength,
            onValueChange = viewModel::onAxialLengthChanged,
            label = { Text("Axial Length (mm)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.k1,
            onValueChange = viewModel::onK1Changed,
            label = { Text("Keratometry K1 (D)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.k2,
            onValueChange = viewModel::onK2Changed,
            label = { Text("Keratometry K2 (D)") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = uiState.aConstant,
            onValueChange = viewModel::onAConstantChanged,
            label = { Text("A-Constant") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(24.dp))

        Button(onClick = { viewModel.calculate() }, modifier = Modifier.fillMaxWidth()) {
            Text("Calculate IOL Power")
        }

        Spacer(Modifier.height(24.dp))

        if (uiState.calculatedIolPower != null) {
            Text(
                text = "Estimated IOL Power: %.2f D".format(uiState.calculatedIolPower),
                style = MaterialTheme.typography.titleLarge
            )
        }

        if (uiState.errorMessage != null) {
            Text(
                text = uiState.errorMessage!!,
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.weight(1f))

        Text(
            text = "DISCLAIMER: This tool is for educational purposes only and must not be used for clinical decisions. Consult a qualified ophthalmologist.",
            style = MaterialTheme.typography.labelSmall,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )
    }
}