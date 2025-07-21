package com.jmr.medhealth.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.jmr.medhealth.ui.articles.ArticleSearchScreen
import com.jmr.medhealth.ui.capture.CaptureScreen
import com.jmr.medhealth.ui.chatbot.ChatScreen
import com.jmr.medhealth.ui.details.DetailsScreen
import com.jmr.medhealth.ui.history.HistoryScreen
import com.jmr.medhealth.ui.home.HomeScreen
import com.jmr.medhealth.ui.iol.IolCalculatorScreen
import com.jmr.medhealth.ui.snellen.SnellenChartScreen
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Visibility
data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, "home"),
        BottomNavItem("Capture", Icons.Default.Camera, "capture"),
        BottomNavItem("History", Icons.Default.History, "history"),
        BottomNavItem("Snellen", Icons.Default.Visibility, "snellen"),
        BottomNavItem("IOL Calc", Icons.Default.Calculate, "iol_calculator"),
        BottomNavItem("AI Agent", Icons.AutoMirrored.Filled.Chat, "chatbot")
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                navItems.forEach { item ->
                    NavigationBarItem(
                        icon = { Icon(item.icon, contentDescription = item.label) },
                        label = { Text(item.label) },
                        selected = currentRoute == item.route,
                        onClick = {
                            navController.navigate(item.route) {
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        MainNavigationHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
fun MainNavigationHost(navController: NavHostController, modifier: Modifier) {
    NavHost(navController, startDestination = "home", modifier = modifier) {
        composable("home") { HomeScreen(navController = navController) } // Pass NavController to HomeScreen
        composable("capture") {
            CaptureScreen(navigateToDetails = { diagnosisId ->
                navController.navigate("details/$diagnosisId")
            })
        }
        composable("history") { HistoryScreen() }
        composable("articles") { ArticleSearchScreen() }
        composable("chatbot") { ChatScreen() }
        composable("snellen") { SnellenChartScreen() } // NEW
        composable("iol_calculator") { IolCalculatorScreen() } // NEW
        composable(
            route = "details/{diagnosisId}",
            arguments = listOf(navArgument("diagnosisId") { type = NavType.IntType })
        ) { backStackEntry ->
            val diagnosisId = backStackEntry.arguments?.getInt("diagnosisId") ?: -1
            DetailsScreen(
                diagnosisId = diagnosisId,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}