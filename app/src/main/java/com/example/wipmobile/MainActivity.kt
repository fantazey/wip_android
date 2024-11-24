package com.example.wipmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.wipmobile.ui.auth.AuthenticationViewModel
import com.example.wipmobile.ui.models.ModelsViewModel
import com.example.wipmobile.ui.theme.WipMobileTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authenticationViewModel: AuthenticationViewModel
    @Inject
    lateinit var modelsViewModel: ModelsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WipApplication).appComponent.inject(this)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WipMobileTheme {
                WipApp(
                    authenticationViewModel=authenticationViewModel,
                    modelsViewModel = modelsViewModel
                )
            }
        }
    }
}
