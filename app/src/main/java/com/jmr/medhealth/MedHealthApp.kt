package com.jmr.medhealth

import android.app.Application
import com.jmr.medhealth.data.repository.RemoteConfigRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The base Application class for MedHealth.
 * Annotated with @HiltAndroidApp to enable Hilt dependency injection.
 */
@HiltAndroidApp
class MedHealthApp : Application() {

    // Inject the RemoteConfigRepository using Hilt.
    // Hilt will provide the singleton instance defined in your AppModule.
    @Inject
    lateinit var remoteConfigRepository: RemoteConfigRepository

    // A custom coroutine scope tied to the application's lifecycle.
    private val applicationScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        // Launch a coroutine to fetch and activate Firebase Remote Config values
        // when the application starts. This makes them available for features like the
        // AI Health Agent as soon as possible.
        applicationScope.launch {
            remoteConfigRepository.fetchAndActivate()
        }
    }
}