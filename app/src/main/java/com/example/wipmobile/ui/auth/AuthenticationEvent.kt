package com.example.wipmobile.ui.auth

sealed class AuthenticationEvent {
    class CheckAuthentication(val successCallback: () -> Unit): AuthenticationEvent()
    class LoginChanged(val login: String): AuthenticationEvent()
    class PasswordChanged(val password: String): AuthenticationEvent()
    class Authenticate(val successCallback: () -> Unit): AuthenticationEvent()
    data object ClearError: AuthenticationEvent()
}