package com.example.wipmobile.ui.auth

import com.example.wipmobile.data.dto.SignUpForm

sealed class AuthenticationEvent {
    class CheckAuthentication(val successCallback: () -> Unit): AuthenticationEvent()
    class Authenticate(val login: String, val password: String, val successCallback: () -> Unit): AuthenticationEvent()
    class SignUp(val form: SignUpForm, val successCallback: () -> Unit): AuthenticationEvent()
    data object ClearError: AuthenticationEvent()
    class Logout(val navigationCallback: () -> Unit): AuthenticationEvent()
}