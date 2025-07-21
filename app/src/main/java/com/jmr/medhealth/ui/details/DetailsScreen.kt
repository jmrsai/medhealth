package com.jmr.medhealth.ui.details

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jmr.medhealth.BuildConfig
import com.jmr.medhealth.util.PdfGenerator
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    diagnosisId: Int,
    viewModel: DetailsViewModel = hiltViewModel(),
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(diagnosisId) {
        viewModel.loadDiagnosis(diagnosisId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Diagnosis Details") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        when (val currentState = state) {
            is DetailsUiState.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is DetailsUiState.Error -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(currentState.message, color = MaterialTheme.colorScheme.error)
                }
            }
            is DetailsUiState.Success -> {
                val diagnosis = currentState.diagnosis
                var notes by remember { mutableStateOf(diagnosis.notes ?: "") }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        AsyncImage(
                            model = diagnosis.imageUrl,
                            contentDescription = "Diagnosis Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Spacer(Modifier.height(16.dp))
                    Text("Result: ${diagnosis.result}", style = MaterialTheme.typography.headlineSmall)
                    Spacer(Modifier.height(8.dp))
                    Text("Confidence: ${"%.2f".format(diagnosis.confidence * 100)}%", style = MaterialTheme.typography.bodyLarge)
                    Spacer(Modifier.height(8.dp))
                    Text("Date: ${SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(Date(diagnosis.timestamp))}", style = MaterialTheme.typography.bodySmall)

                    if (diagnosis.description != null) {
                        Text("Description:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                        Text(diagnosis.description, style = MaterialTheme.typography.bodyMedium)
                    }
                    if (diagnosis.suggestedNextSteps != null) {
                        Text("Suggested Next Steps:", style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(top = 16.dp))
                        Text(diagnosis.suggestedNextSteps, style = MaterialTheme.typography.bodyMedium)
                    }

                    Spacer(Modifier.height(16.dp))

                    OutlinedTextField(
                        value = notes,
                        onValueChange = { notes = it },
                        label = { Text("Your Personal Notes") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                    )
                    Spacer(Modifier.height(8.dp))
                    Button(
                        onClick = { viewModel.saveNotes(diagnosis.id, notes) },
                        modifier = Modifier.align(Alignment.End)
                    ) {
                        Text("Save Note")
                    }

                    Spacer(Modifier.weight(1f))
                    Button(
                        onClick = {
                            // Note: PDF generation needs a real bitmap, which isn't available here.
                            // This part remains conceptual.
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                        Spacer(Modifier.width(8.dp))
                        Text("Share Report (Conceptual)")
                    }
                }
            }
        }
    }
}

private fun sharePdf(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.provider", file)
    val intent = Intent(Intent.ACTION_SEND).apply {
        type = "application/pdf"
        putExtra(Intent.EXTRA_STREAM, uri)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    context.startActivity(Intent.createChooser(intent, "Share Report"))
}