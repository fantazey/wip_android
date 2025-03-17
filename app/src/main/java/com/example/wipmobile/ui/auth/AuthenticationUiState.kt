package com.example.wipmobile.ui.auth

data class AuthenticationUiState(
    val login: String? = null,
    val password: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val authenticated: Boolean = false,
    val authChecked: Boolean = false
) {
    fun isFormValid(): Boolean {
        return password?.isNotBlank() == true && login?.isNotBlank() == true
    }
}
