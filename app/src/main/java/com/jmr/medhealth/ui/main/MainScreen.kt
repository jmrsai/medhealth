package com.jmr.medhealth.ui.main

import androidx.compose.runtime.Composable
import androidx.navigation.compose.*
import com.jmr.medhealth.ui.capture.CaptureScreen
import com.jmr.medhealth.ui.details.DetailsScreen
import com.jmr.medhealth.ui.documents.DocumentUploaderScreen
import com.jmr.medhealth.ui.history.HistoryScreen
import com.jmr.medhealth.ui.home.HomeScreen
import com.jmr.medhealth.ui.iol.IolCalculatorScreen
import com.jmr.medhealth.ui.snellen.SnellenChartScreen

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController = navController) }
        composable("capture") { CaptureScreen(navigateToDetails = { id -> navController.navigate("details/$id") }) }
        composable("history") { HistoryScreen() } // Add navController if needed for clicks
        composable("snellen") { SnellenChartScreen() }
        composable("iol_calculator") { IolCalculatorScreen() }
        composable("upload_document") { DocumentUploaderScreen(onUploadComplete = { navController.popBackStack() }) }
        composable(
            route = "details/{diagnosisId}",
            arguments = listOf(navArgument("diagnosisId") { type = NavType.IntType })
        ) {
            val diagnosisId = it.arguments?.getInt("diagnosisId") ?: -1
            DetailsScreen(diagnosisId = diagnosisId, onNavigateBack = { navController.popBackStack() })
        }
    }
}