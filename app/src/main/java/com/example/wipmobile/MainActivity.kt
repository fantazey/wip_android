package com.example.wipmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wipmobile.ui.theme.WipMobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WipMobileTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    LoginForm(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun LoginForm(name: String, modifier: Modifier = Modifier) {
    val login = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    Column {
        TextField(
            value = login.value,
            onValueChange = {login.value = it},
            label = { Text("Логин") },
            modifier = Modifier.fillMaxWidth()
        )
        TextField(
            value = password.value,
            onValueChange = {password.value = it},
            label = { Text("Пароль") },
            modifier = Modifier.fillMaxWidth()
        )
        Button( onClick = {}, modifier = Modifier.fillMaxWidth() ) { Text("Вход") }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginFormPreview() {
    WipMobileTheme {
        LoginForm("Android")
    }
}