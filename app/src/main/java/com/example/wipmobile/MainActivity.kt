package com.example.wipmobile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.example.wipmobile.ui.add_model.AddModelViewModel
import com.example.wipmobile.ui.auth.AuthenticationViewModel
import com.example.wipmobile.ui.model.ModelViewModel
import com.example.wipmobile.ui.models.ModelsViewModel
import com.example.wipmobile.ui.theme.WipMobileTheme
import javax.inject.Inject

class MainActivity : ComponentActivity() {
    @Inject
    lateinit var authenticationViewModel: AuthenticationViewModel
    @Inject
    lateinit var modelsViewModel: ModelsViewModel
    @Inject
    lateinit var addModelViewModel: AddModelViewModel
    @Inject
    lateinit var modelViewModel: ModelViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as WipApplication).appComponent.inject(this)
        Glide.get(this).setMemoryCategory(MemoryCategory.HIGH)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WipMobileTheme {
                WipApp(
                    authenticationViewModel=authenticationViewModel,
                    modelsViewModel = modelsViewModel,
                    addModelViewModel = addModelViewModel,
                    modelViewModel = modelViewModel
                )
            }
        }
    }
}
