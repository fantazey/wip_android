package com.example.wipmobile

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wipmobile.data.AuthenticationUiState
import com.example.wipmobile.data.ModelsUiState
import com.example.wipmobile.ui.AuthenticationScreen
import com.example.wipmobile.ui.ModelsScreen
import com.example.wipmobile.ui.auth.AuthenticationEvent
import com.example.wipmobile.ui.auth.AuthenticationViewModel
import com.example.wipmobile.ui.models.ModelsEvent
import com.example.wipmobile.ui.models.ModelsViewModel


enum class WipScreen(@StringRes val title: Int) {
    Authentication(R.string.login_screen_title),
    Models(R.string.model_list_screen_title)
}


@Composable
fun WipApp(
    authenticationViewModel: AuthenticationViewModel = viewModel(),
    modelsViewModel: ModelsViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WipScreen.valueOf(
        backStackEntry?.destination?.route ?: WipScreen.Authentication.name
    )
    Log.i("wip screen", "currentScreen: ${currentScreen.name}")
    val authUiState by authenticationViewModel.uiState.collectAsState()
    val modelsUiState by modelsViewModel.uiState.collectAsState()
    val authViewModelEventHandler: (e: AuthenticationEvent) -> Unit = { authenticationViewModel.handleEvent(it) }
    val modelsViewModelEventHandler: (e: ModelsEvent) -> Unit = { modelsViewModel.handleEvent(it) }
    WipAppScreen(
        authUiState = authUiState,
        modelsUiState = modelsUiState,
        authViewModelEventHandler = authViewModelEventHandler,
        modelsViewModelEventHandler = modelsViewModelEventHandler,
        currentScreen = currentScreen,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WipAppScreen(
    authUiState: AuthenticationUiState,
    modelsUiState: ModelsUiState,
    authViewModelEventHandler: (e: AuthenticationEvent) -> Unit,
    modelsViewModelEventHandler: (e: ModelsEvent) -> Unit,
    currentScreen: WipScreen,
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = {
            if (WipScreen.Authentication != currentScreen) {
                NavigationBar() {
                    NavigationBarItem(
                        selected = WipScreen.Models == currentScreen,
                        onClick = { navController.navigate(WipScreen.Models.name) },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.List,
                                contentDescription = null,
                            )
                        },
                        label = {
                            Text("Список моделей")
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = WipScreen.Authentication.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = WipScreen.Authentication.name) {
                AuthenticationScreen(
                    authenticationState = authUiState,
                    successAuthCallback = {
                        navController.navigate(WipScreen.Models.name)
                    },
                    handleEvent = authViewModelEventHandler,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            composable(route = WipScreen.Models.name) {
                ModelsScreen(
                    modelsUiState = modelsUiState,
                    handleEvent = modelsViewModelEventHandler
                )
            }
        }
    }
}


@Preview
@Composable
fun WinAppScreenPreview() {
    val navController = rememberNavController()
    val modelsUiState = ModelsUiState(
        isLoading = false,
        modelResponses = emptyArray(),
        modelsLoaded = true
    )
    WipAppScreen(
        authUiState = AuthenticationUiState(),
        modelsUiState = modelsUiState,
        authViewModelEventHandler = {},
        modelsViewModelEventHandler = {},
        currentScreen = WipScreen.Authentication,
        navController = navController
    )
}