package com.example.wipmobile.ui.auth

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wipmobile.R

fun checkAuthData(login: String, password: String): Boolean {
    return password.isNotBlank() && login.isNotBlank()
}


@Composable
fun AuthenticationForm(
    modifier: Modifier,
    onAuthenticate: (login: String, password: String) -> Unit,
    onRegister: (login: String, password: String) -> Unit
) {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var enableAuthentication by remember { mutableStateOf(false) }
    var showRegisterForm by remember { mutableStateOf(false) }
    val checkAuthData = { loginValue: String, passwordValue: String ->
        if (loginValue.isNotBlank() && passwordValue.isNotBlank()) {
            enableAuthentication = true
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        if (showRegisterForm) {
            RegisterTitle()
        } else {
            AuthenticationTitle()
        }
        Spacer(modifier = Modifier.height(40.dp))
        val passwordFocusRequester = FocusRequester()
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginInput(
                    modifier = Modifier.fillMaxWidth(),
                    login = login,
                    onLoginChanged = { newVal ->
                        login = newVal
                        checkAuthData(login, password)
                    }
                ) {
                    passwordFocusRequester.requestFocus()
                }
                Spacer(modifier = Modifier.height(16.dp))
                PasswordInput(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester = passwordFocusRequester),
                    password = password,
                    onPasswordChanged = { newValue: String ->
                        password = newValue
                        checkAuthData(login, password)
                    },
                    onDoneClicked = {
                        onAuthenticate(login, password)
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                AuthenticationButton(
                    enabled = if (showRegisterForm) true else enableAuthentication,
                    onClick = {
                        if (showRegisterForm) {
                            onRegister(login, password)
                        } else {
                            onAuthenticate(login, password)
                        }
                    },
                    title = if (showRegisterForm) {
                        R.string.register_button_label
                    } else {
                        R.string.login_in_button_label
                    }
                )
                TextButton(onClick = {showRegisterForm = !showRegisterForm}) {
                    if (showRegisterForm) {
                        Text("Вход")
                    } else {
                        Text("Регистрация")
                    }
                }
            }
        }
    }
}

@Composable
fun AuthenticationTitle() {
    AuthenticationScreenTitle(R.string.title_activity_login)
}

@Composable
fun RegisterTitle() {
    AuthenticationScreenTitle(R.string.title_activity_register)
}

@Composable
fun AuthenticationScreenTitle(@StringRes id: Int) {
    Text(
        text = stringResource(id),
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
    )
}


@Composable
fun LoginInput(
    modifier: Modifier = Modifier,
    login: String,
    onLoginChanged: (newLogin: String) -> Unit,
    onNextClicked: () -> Unit
) {
    TextField(
        modifier = modifier,
        value = login,
        onValueChange = { newLogin ->
            onLoginChanged(newLogin)
        },
        label = {
            Text(stringResource(R.string.prompt_login))
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = null
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNextClicked() }
        )
    )
}

@Composable
fun PasswordInput(
    modifier: Modifier = Modifier,
    password: String,
    onPasswordChanged: (newValue: String) -> Unit,
    onDoneClicked: () -> Unit
) {
    var isHidden by remember {
        mutableStateOf(true)
    }
    TextField(
        modifier = modifier,
        value = password,
        onValueChange = {
            onPasswordChanged(it)
        },
        label = {
            Text(stringResource(R.string.prompt_password))
        },
        singleLine = true,
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Password,
                contentDescription = null
            )
        },
        visualTransformation = if (isHidden) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            Icon(
                imageVector = if (isHidden) {
                    Icons.Default.Visibility
                } else {
                    Icons.Default.VisibilityOff
                },
                contentDescription = null,
                modifier = Modifier.clickable(
                    onClickLabel = if (isHidden) {
                        stringResource(R.string.cd_password_visibility_show)
                    } else {
                        stringResource(R.string.cd_password_visibility_hide)
                    }
                ) {
                    isHidden = !isHidden
                }
            )
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                onDoneClicked()
            }
        )
    )
}

@Composable
fun AuthenticationButton(
    modifier: Modifier = Modifier,
    @StringRes title: Int,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = {
            onClick()
        }
    ) {
        Text(text = stringResource(title))
    }
}