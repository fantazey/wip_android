package com.example.wipmobile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.ui.add_model.AddModelEvent
import com.example.wipmobile.ui.add_model.AddModelFormContainer
import com.example.wipmobile.ui.add_model.AddModelUiState

@Composable
fun AddModelScreen(
    uiState: AddModelUiState,
    handleEvent: (event: AddModelEvent) -> Unit,
    successCallback: (model: Model) -> Unit,
    modifier: Modifier = Modifier
) {
    if (!uiState.loaded && !uiState.isLoading) {
        handleEvent(AddModelEvent.LoadData)
    }
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            AddModelFormContainer(
                uiState = uiState,
                handleEvent = handleEvent,
                successCallback = successCallback,
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