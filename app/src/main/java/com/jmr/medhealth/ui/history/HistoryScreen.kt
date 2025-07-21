package com.jmr.medhealth.ui.history

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.jmr.medhealth.ui.main.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: MainViewModel = hiltViewModel()) {
    val history by viewModel.diagnosisHistory.collectAsState(initial = emptyList())

    LaunchedEffect(Unit) {
        viewModel.syncHistory()
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Diagnosis History") }) }) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(history) { diagnosis ->
                Card(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                        .clickable { /* Navigate to detail screen */ },
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = diagnosis.imageUrl,
                            contentDescription = "Diagnosis Image",
                            modifier = Modifier.size(60.dp),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(diagnosis.result, style = MaterialTheme.typography.titleMedium)
                            Text("Confidence: ${"%.2f".format(diagnosis.confidence * 100)}%")
                        }
                    }
                }
            }
        }
    }
}