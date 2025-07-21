package com.jmr.medhealth // Updated

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.jmr.medhealth.ui.navigation.AppNavigation // Updated
import com.jmr.medhealth.ui.theme.MedHealthTheme // Updated
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MedHealthTheme { // Updated
                AppNavigation()
            }
        }
    }
}