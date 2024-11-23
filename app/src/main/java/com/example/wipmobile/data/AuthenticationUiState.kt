package com.example.wipmobile.data

data class AuthenticationUiState(
    val login: String? = null,
    val password: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val authenticated: Boolean = false
) {
    fun isFormValid(): Boolean {
        return password?.isNotBlank() == true && login?.isNotBlank() == true
    }
}
