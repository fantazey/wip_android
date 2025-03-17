package com.example.wipmobile.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wipmobile.ui.components.ErrorDialog
import com.example.wipmobile.ui.model.ModelCard
import com.example.wipmobile.ui.model.ModelEvent
import com.example.wipmobile.ui.model.ModelUiState

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ModelScreen(
    uiState: ModelUiState,
    handleEvent: (event: ModelEvent) -> Unit,
    navigateBackCallback: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.error != null) {
            ErrorDialog(
                error = uiState.error,
                clearError = { handleEvent(ModelEvent.ClearError) }
            )
        } else if (null == uiState.model) {
            ErrorDialog(
                error = "Ошибка. Модель не загружена",
                clearError = { handleEvent(ModelEvent.ClearError) }
            )
        } else {
            ModelCard(
                uiState = uiState,
                handleEvent = handleEvent,
                navigateBackCallback = navigateBackCallback
            )
        }
    }
}