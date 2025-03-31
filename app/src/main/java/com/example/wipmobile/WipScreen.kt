package com.example.wipmobile

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.ui.AddModelScreen
import com.example.wipmobile.ui.auth.AuthenticationUiState
import com.example.wipmobile.ui.models.ModelsUiState
import com.example.wipmobile.ui.AuthenticationScreen
import com.example.wipmobile.ui.ModelScreen
import com.example.wipmobile.ui.ModelsScreen
import com.example.wipmobile.ui.ProfileScreen
import com.example.wipmobile.ui.ProgressScreen
import com.example.wipmobile.ui.WorksScreen
import com.example.wipmobile.ui.add_model.AddModelEvent
import com.example.wipmobile.ui.add_model.AddModelUiState
import com.example.wipmobile.ui.add_model.AddModelViewModel
import com.example.wipmobile.ui.auth.AuthenticationEvent
import com.example.wipmobile.ui.auth.AuthenticationViewModel
import com.example.wipmobile.ui.components.BottomNavigationBar
import com.example.wipmobile.ui.model.ModelEvent
import com.example.wipmobile.ui.model.ModelUiState
import com.example.wipmobile.ui.model.ModelViewModel
import com.example.wipmobile.ui.models.ModelsEvent
import com.example.wipmobile.ui.models.ModelsViewModel
import com.example.wipmobile.ui.profile.ProfileEvent
import com.example.wipmobile.ui.profile.ProfileUiState
import com.example.wipmobile.ui.profile.ProfileViewModel


enum class WipScreen(@StringRes val title: Int) {
    Authentication(R.string.login_screen_title),
    Models(R.string.model_list_screen_title),
    AddModel(R.string.add_model_screen_title),
    Works(R.string.works_screen_title),
    Profile(R.string.profile_screen_title),
    Model(R.string.model_screen_title),
    Progress(R.string.model_progress_screen_title)
}


@Composable
fun WipApp(
    authenticationViewModel: AuthenticationViewModel = viewModel(),
    modelsViewModel: ModelsViewModel = viewModel(),
    addModelViewModel: AddModelViewModel = viewModel(),
    modelViewModel: ModelViewModel = viewModel(),
    profileViewModel: ProfileViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = WipScreen.valueOf(
        backStackEntry?.destination?.route ?: WipScreen.Authentication.name
    )
    Log.i("wip screen", "currentScreen: ${currentScreen.name}")
    val authUiState by authenticationViewModel.uiState.collectAsState()
    val authViewModelEventHandler: (e: AuthenticationEvent) -> Unit =
        { authenticationViewModel.handleEvent(it) }

    val modelsUiState by modelsViewModel.uiState.collectAsState()
    val modelsViewModelEventHandler: (e: ModelsEvent) -> Unit = { modelsViewModel.handleEvent(it) }

    val addModelUiState by addModelViewModel.uiState.collectAsState()
    val addModelEventHandler: (e: AddModelEvent) -> Unit = { addModelViewModel.handleEvent(it) }

    val modelUiState by modelViewModel.uiState.collectAsState()
    val modelViewModelEventHandler: (e: ModelEvent) -> Unit = { modelViewModel.handleEvent(it) }

    val profileUiState by profileViewModel.uiState.collectAsState()
    val profileViewModelEventHandler: (e: ProfileEvent) -> Unit =
        { profileViewModel.handleEvent(it) }
    WipAppScreen(
        authUiState = authUiState,
        modelsUiState = modelsUiState,
        addModelUiState = addModelUiState,
        modelUiState = modelUiState,
        profileUiState = profileUiState,
        authViewModelEventHandler = authViewModelEventHandler,
        modelsViewModelEventHandler = modelsViewModelEventHandler,
        modelViewModelEventHandler = modelViewModelEventHandler,
        addModelEventHandler = addModelEventHandler,
        profileViewModelEventHandler = profileViewModelEventHandler,
        currentScreen = currentScreen,
        navController = navController
    )
}


@Composable
fun WipAppScreen(
    authUiState: AuthenticationUiState,
    modelsUiState: ModelsUiState,
    modelUiState: ModelUiState,
    addModelUiState: AddModelUiState,
    profileUiState: ProfileUiState,
    authViewModelEventHandler: (e: AuthenticationEvent) -> Unit,
    modelsViewModelEventHandler: (e: ModelsEvent) -> Unit,
    modelViewModelEventHandler: (e: ModelEvent) -> Unit,
    addModelEventHandler: (e: AddModelEvent) -> Unit,
    profileViewModelEventHandler: (e: ProfileEvent) -> Unit,
    currentScreen: WipScreen,
    navController: NavHostController,
) {
    Scaffold(
        bottomBar = {
            if (WipScreen.Authentication != currentScreen) {
                BottomNavigationBar(currentScreen) { screen -> navController.navigate(screen) }
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
                    uiState = modelsUiState,
                    handleEvent = modelsViewModelEventHandler,
                    selectModel = { model: Model ->
                        modelViewModelEventHandler(ModelEvent.Select(model))
                        navController.navigate(WipScreen.Model.name)
                    }
                )
            }
            composable(route = WipScreen.Model.name) {
                ModelScreen(
                    uiState = modelUiState,
                    handleEvent = modelViewModelEventHandler,
                    navigateBackCallback = {
                        navController.navigate(WipScreen.Models.name)
                    },
                    navigateToProgressCallback = {
                        navController.navigate(WipScreen.Progress.name)
                    }
                )
            }
            composable(route = WipScreen.Progress.name) {
                ProgressScreen(
                    uiState = modelUiState,
                    handleEvent = modelViewModelEventHandler,
                    navigateBackCallback = { model: Model, tab: Int ->
                        modelViewModelEventHandler(ModelEvent.Select(model, tab))
                        navController.navigate(WipScreen.Model.name)
                    }
                )
            }
            composable(route = WipScreen.Works.name) {
                WorksScreen()
            }
            composable(route = WipScreen.AddModel.name) {
                AddModelScreen(
                    uiState = addModelUiState,
                    handleEvent = addModelEventHandler,
                    successCallback = { model ->
                        modelViewModelEventHandler(ModelEvent.Select(model))
                        navController.navigate(WipScreen.Model.name)
                    },
                    cancelEditCallback = {
                        navController.navigate(WipScreen.Models.name)
                    }
                )
            }
            composable(route = WipScreen.Profile.name) {
                ProfileScreen(
                    uiState = profileUiState,
                    handleEvent = profileViewModelEventHandler,
                    logout = {
                        authViewModelEventHandler(AuthenticationEvent.Logout(navigationCallback = {
                            modelsViewModelEventHandler(ModelsEvent.ResetLoaded)
                            navController.navigate(WipScreen.Authentication.name)
                        }))
                    },
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
        models = emptyList(),
        loaded = true
    )
    WipAppScreen(
        authUiState = AuthenticationUiState(),
        modelsUiState = modelsUiState,
        addModelUiState = AddModelUiState(),
        modelUiState = ModelUiState(),
        profileUiState = ProfileUiState(),
        authViewModelEventHandler = {},
        modelsViewModelEventHandler = {},
        addModelEventHandler = {},
        modelViewModelEventHandler = {},
        profileViewModelEventHandler = {},
        currentScreen = WipScreen.Profile,
        navController = navController
    )
}