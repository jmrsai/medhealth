package com.jmr.medhealth.data.repository

import com.google.firebase.auth.FirebaseUser
import com.jmr.medhealth.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val currentUser: FirebaseUser?
    val hasUser: Boolean

    fun getAuthState(): Flow<FirebaseUser?>
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(user: User, password: String): Result<Unit>
    fun logout()

}