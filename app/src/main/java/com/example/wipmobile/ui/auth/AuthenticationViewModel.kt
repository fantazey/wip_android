package com.example.wipmobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.AuthenticationUiState
import com.example.wipmobile.data.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel() {
    val uiState = MutableStateFlow(AuthenticationUiState())

    fun handleEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.LoginChanged -> {
                updateLogin(event.login)
            }
            is AuthenticationEvent.PasswordChanged -> {
                updatePassword(event.password)
            }
            is AuthenticationEvent.Authenticate -> {
                authenticate(event.successCallback)
            }
            is AuthenticationEvent.ClearError -> {
                clearError()
            }
            is AuthenticationEvent.CheckAuthentication -> {
                checkToken(event.successCallback)
            }
        }
    }

    private fun checkToken(callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        var authenticated = false
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authenticated = userRepository.isUserLogged()
            }
            if (authenticated) {
                callback()
            }
            uiState.update {
                it.copy(authenticated = authenticated, isLoading = false)
            }
        }
    }

    private fun updateLogin(login: String) {
        uiState.value = uiState.value.copy(login=login)
    }

    private fun updatePassword(password: String) {
        uiState.value = uiState.value.copy(password=password)
    }

    private fun authenticate(successAuthCallback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.login(uiState.value.login!!, uiState.value.password!!)
                uiState.value = uiState.value.copy(isLoading = false, authenticated = true)
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(isLoading = false, error = ex.message, authenticated = false)
                }
            }
        }
        if (uiState.value.authenticated) {
            successAuthCallback()
        }
    }

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }
}