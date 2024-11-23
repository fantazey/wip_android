package com.example.wipmobile

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wipmobile.ui.AuthenticationScreen
import com.example.wipmobile.ui.auth.AuthenticationViewModel

enum class WipScreen(@StringRes val title: Int) {
    Authentication(R.string.login_screen_title),
    Models(R.string.model_list_screen_title)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WipAppBar(
    currentScreen: WipScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        title = { Text(stringResource(id=currentScreen.title)) },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(imageVector = Icons.Filled.ArrowBackIosNew,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
            }
        }
    )
}

@Composable
fun WipApp(
    authenticationViewModel: AuthenticationViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WipScreen.valueOf(
        backStackEntry?.destination?.route ?: WipScreen.Authentication.name
    )
    Scaffold(
        topBar = {
            WipAppBar(
                currentScreen = WipScreen.Authentication,
                canNavigateBack = navController.previousBackStackEntry != null,
                navigateUp = { navController.navigateUp() }
            )
        }
    ) { innerPadding ->
        val uiState by authenticationViewModel.uiState.collectAsState()
        NavHost(
            navController=navController,
            startDestination = WipScreen.Authentication.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = WipScreen.Authentication.name) {
                AuthenticationScreen(
                    authenticationState = uiState,
                    successAuthCallback = {
                        navController.navigate(WipScreen.Models.name)
                    },
                    handleEvent = {e ->
                        authenticationViewModel.handleEvent(e)
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
            composable(route = WipScreen.Models.name) {
                Text("Hello")
            }
        }
    }
}