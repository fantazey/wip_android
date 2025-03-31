package com.example.wipmobile.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.UserRepository
import com.example.wipmobile.data.dto.SignUpForm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthenticationViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {
    val uiState = MutableStateFlow(AuthenticationUiState())

    fun handleEvent(event: AuthenticationEvent) {
        when (event) {
            is AuthenticationEvent.Authenticate -> {
                authenticate(event.login, event.password, event.successCallback)
            }

            is AuthenticationEvent.SignUp -> {
                singUp(event.form, event.successCallback)
            }

            is AuthenticationEvent.ClearError -> {
                clearError()
            }

            is AuthenticationEvent.CheckAuthentication -> {
                checkToken(event.successCallback)
            }

            is AuthenticationEvent.Logout -> {
                logout(event.navigationCallback)
            }
        }
    }

    private fun checkToken(callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        var authenticated: Boolean
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                authenticated = userRepository.isUserLogged()
            }
            if (authenticated) {
                callback()
            }
            uiState.update {
                it.copy(authenticated = authenticated, isLoading = false, authChecked = true)
            }
        }
    }

    private fun authenticate(login: String, password: String, successAuthCallback: () -> Unit) {
        if (uiState.value.authenticated) {
            successAuthCallback()
        }
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.login(login, password)
                uiState.value = uiState.value.copy(isLoading = false, authenticated = true)
                withContext(Dispatchers.Main) {
                    successAuthCallback()
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = ex.message,
                        authenticated = false
                    )
                }
            }
        }
    }

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }

    private fun logout(callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.logout()
                uiState.value = uiState.value.copy(isLoading = false, authenticated = false)
                withContext(Dispatchers.Main) {
                    callback()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = e.message,
                        authenticated = false
                    )
                }
            }
        }
    }

    private fun singUp(form: SignUpForm, callback: () -> Unit) {
        if (uiState.value.authenticated) {
            callback()
        }
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userRepository.signUp(form)
                uiState.value = uiState.value.copy(isLoading = false, authenticated = true)
                withContext(Dispatchers.Main) {
                    callback()
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        isLoading = false,
                        error = ex.message,
                        authenticated = false
                    )
                }
            }
        }
    }
}