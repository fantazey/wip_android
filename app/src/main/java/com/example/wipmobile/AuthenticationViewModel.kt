package com.example.wipmobile

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow

class AuthenticationViewModel: ViewModel() {
    val uiState = MutableStateFlow(AuthenticationState())

    fun handleEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.LoginChanged -> {
                updateLogin(event.login)
            }
            is AuthenticationEvent.PasswordChanged -> {
                updatePassword(event.password)
            }
            is AuthenticationEvent.Authenticate -> {
                authenticate()
            }
        }
    }

    private fun updateLogin(login: String) {
        uiState.value = uiState.value.copy(login=login)
    }

    private fun updatePassword(password: String) {
        uiState.value = uiState.value.copy(password=password)
    }

    private fun authenticate() {
        uiState.value = uiState.value.copy(isLoading = true)
    }
}