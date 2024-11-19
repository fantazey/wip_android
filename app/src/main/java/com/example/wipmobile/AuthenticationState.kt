package com.example.wipmobile

data class AuthenticationState(
    val login: String? = null,
    val password: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null
) {
    fun isFormValid(): Boolean {
        return password?.isNotBlank() == true && login?.isNotBlank() == true
    }
}
