package com.jmr.medhealth.ui.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jmr.medhealth.ui.auth.AuthViewModel
import com.jmr.medhealth.ui.auth.LoginScreen
import com.jmr.medhealth.ui.auth.RegisterScreen
import com.jmr.medhealth.ui.main.MainScreen
import com.jmr.medhealth.ui.splash.SplashScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = hiltViewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("login") {
            LoginScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("register") {
            RegisterScreen(navController = navController, authViewModel = authViewModel)
        }
        composable("main") {
            MainScreen()
        }
    }
}