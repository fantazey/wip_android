package com.example.wipmobile.ui.auth

data class AuthenticationUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val authenticated: Boolean = false,
    val authChecked: Boolean = false
)