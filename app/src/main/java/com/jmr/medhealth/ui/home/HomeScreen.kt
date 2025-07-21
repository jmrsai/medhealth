package com.jmr.medhealth.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class DashboardItem(
    val title: String,
    val icon: ImageVector,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    val dashboardItems = listOf(
        DashboardItem("AI Diagnosis", Icons.Default.Camera, "capture"),
        DashboardItem("Diagnosis History", Icons.Default.History, "history"),
        DashboardItem("AI Health Agent", Icons.AutoMirrored.Filled.Chat, "chatbot"),
        DashboardItem("Upload Document", Icons.Default.CloudUpload, "upload_document"),
        DashboardItem("Snellen Chart", Icons.Default.Visibility, "snellen"),
        DashboardItem("IOL Calculator", Icons.Default.Calculate, "iol_calculator")
    )

    Scaffold(
        topBar = { TopAppBar(title = { Text("MedHealth Dashboard") }) }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp)
        ) {
            Text("Welcome!", style = MaterialTheme.typography.headlineMedium)
            Text("Select a tool to get started.", style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(24.dp))
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(dashboardItems) { item ->
                    DashboardCard(item = item, onClick = { navController.navigate(item.route) })
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardCard(item: DashboardItem, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = item.icon,
                contentDescription = item.title,
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(12.dp))
            Text(
                text = item.title,
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center
            )
        }
    }
}