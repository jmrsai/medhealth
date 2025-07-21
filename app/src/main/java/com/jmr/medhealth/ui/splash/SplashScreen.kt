package com.jmr.medhealth.ui.splash

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.jmr.medhealth.ui.auth.AuthState
import com.jmr.medhealth.ui.auth.AuthViewModel

@Composable
fun SplashScreen(navController: NavController, authViewModel: AuthViewModel) {
    val authState = authViewModel.authState.collectAsState().value

    LaunchedEffect(authState) {
        when (authState) {
            is AuthState.Authenticated -> {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            is AuthState.Unauthenticated -> {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
            else -> { /* Stay on splash while loading */ }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}