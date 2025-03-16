package com.example.wipmobile.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wipmobile.ui.add_model.AddModelEvent
import com.example.wipmobile.ui.add_model.AddModelFormContainer
import com.example.wipmobile.ui.add_model.AddModelUiState

@Composable
fun AddModelScreen(
    uiState: AddModelUiState,
    handleEvent: (event: AddModelEvent) -> Unit,
    successCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (!uiState.loaded && !uiState.isLoading) {
        Log.i("model screen", "Список пустой, надо загрузить")
        handleEvent(AddModelEvent.LoadData)
    }
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            AddModelFormContainer(
                uiState = uiState,
                handleEvent = handleEvent,
                modifier = modifier
            )
        }
    }
}

@Composable
@Preview
fun AddModelScreenPreview() {
    AddModelScreen(
        uiState = AddModelUiState(),
        handleEvent = {},
        successCallback = {}
    )
}