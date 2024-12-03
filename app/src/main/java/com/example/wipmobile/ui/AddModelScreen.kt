package com.example.wipmobile.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wipmobile.ui.add_model.AddModelEvent
import com.example.wipmobile.ui.add_model.AddModelForm
import com.example.wipmobile.ui.add_model.AddModelUiState

@Composable
fun AddModelScreen(
    uiState: AddModelUiState,
    eventHandler: (event: AddModelEvent) -> Unit,
    successCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        if (uiState.isLoading) {
            CircularProgressIndicator()
        } else {
            AddModelForm(
                uiState = uiState,
                eventHandler = eventHandler,
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
        eventHandler = {},
        successCallback = {}
    )
}