package com.jmr.medhealth.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jmr.medhealth.data.repository.AuthRepository
import com.jmr.medhealth.domain.model.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class AuthState {
    object Idle : AuthState()
    object Loading : AuthState()
    object Authenticated : AuthState()
    data class Unauthenticated(val error: String? = null) : AuthState()
}

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val repository: AuthRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
    val authState = _authState.asStateFlow()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            repository.getAuthState().collect { user ->
                _authState.value = if (user != null) AuthState.Authenticated else AuthState.Unauthenticated()
            }
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val result = repository.login(email, password)
            if (result.isFailure) {
                _authState.value = AuthState.Unauthenticated(result.exceptionOrNull()?.message)
            }
        }
    }

    fun register(name: String, email: String, password: String) {
        viewModelScope.launch {
            _authState.value = AuthState.Loading
            val user = User(name = name, email = email)
            val result = repository.register(user, password)
            if (result.isFailure) {
                _authState.value = AuthState.Unauthenticated(result.exceptionOrNull()?.message)
            }
        }
    }

    fun logout() {
        repository.logout()
    }
}