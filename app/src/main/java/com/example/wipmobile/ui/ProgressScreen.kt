package com.example.wipmobile.ui

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.ui.components.ErrorDialog
import com.example.wipmobile.ui.model.ModelEvent
import com.example.wipmobile.ui.model.ModelUiState
import com.example.wipmobile.ui.model.ProgressCard

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ProgressScreen(
    uiState: ModelUiState,
    handleEvent: (event: ModelEvent) -> Unit,
    navigateBackCallback: (model: Model, tab: Int) -> Unit
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
            ProgressCard(
                uiState = uiState,
                handleEvent = handleEvent,
                navigateBackCallback = {
                    navigateBackCallback(uiState.model, 1)
                }
            )
        }
    }
}