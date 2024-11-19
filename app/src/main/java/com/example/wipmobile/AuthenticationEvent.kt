package com.example.wipmobile

sealed class AuthenticationEvent {
    class LoginChanged(val login: String): AuthenticationEvent()
    class PasswordChanged(val password: String): AuthenticationEvent()
    object Authenticate: AuthenticationEvent()
}