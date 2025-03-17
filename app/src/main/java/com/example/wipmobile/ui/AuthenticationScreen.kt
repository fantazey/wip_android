package com.example.wipmobile.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wipmobile.ui.auth.AuthenticationUiState
import com.example.wipmobile.ui.auth.AuthenticationEvent
import com.example.wipmobile.ui.auth.AuthenticationForm
import com.example.wipmobile.ui.components.ErrorDialog
import com.example.wipmobile.ui.theme.WipMobileTheme

@Composable
fun AuthenticationScreen(
    authenticationState: AuthenticationUiState,
    modifier: Modifier = Modifier,
    handleEvent: (event: AuthenticationEvent) -> Unit,
    successAuthCallback: () -> Unit
) {
    if (!authenticationState.authChecked && !authenticationState.isLoading) {
        Log.i("auth screen", "check auth")
        handleEvent(AuthenticationEvent.CheckAuthentication(successAuthCallback))
        Log.i("auth screen", "render form")
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (authenticationState.isLoading) {
            CircularProgressIndicator()
        } else {
            AuthenticationForm(
                modifier = Modifier.fillMaxSize(),
                login = authenticationState.login,
                password = authenticationState.password,
                enableAuthentication = authenticationState.isFormValid(),
                onLoginChanged = { newLogin ->
                    handleEvent(AuthenticationEvent.LoginChanged(newLogin))
                },
                onPasswordChanged = {
                    handleEvent(AuthenticationEvent.PasswordChanged(it))
                },
                onAuthenticate = {
                    handleEvent(AuthenticationEvent.Authenticate(successAuthCallback))
                }
            )
            authenticationState.error?.let {
                ErrorDialog(
                    error = it,
                    clearError = {
                        handleEvent(AuthenticationEvent.ClearError)
                    }
                )
            }
        }
    }
}


@Composable
@Preview
fun AuthenticationScreenPreview() {
    val uiState = AuthenticationUiState(
        isLoading = true,
        login = "test",
        password = "asdasdasd",
        error = null
    )
    WipMobileTheme {
        AuthenticationScreen(
            authenticationState = uiState,
            handleEvent = {},
            successAuthCallback = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}
