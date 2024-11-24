package com.example.wipmobile.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wipmobile.R
import com.example.wipmobile.data.ModelsUiState
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.ui.models.ModelsEvent
import com.example.wipmobile.ui.theme.WipMobileTheme

@Composable
fun ModelsScreen(
    modelsUiState: ModelsUiState,
    handleEvent: (event: ModelsEvent) -> Unit
) {
    Log.i("model screen","Рисуем список моделей")
    if (!modelsUiState.modelsLoaded && !modelsUiState.isLoading) {
        Log.i("model screen","Список пустой, надо загрузить")
        handleEvent(ModelsEvent.ModelsLoad)
    }
    Box(modifier = Modifier.fillMaxSize()) {
        if (modelsUiState.isLoading) {
            CircularProgressIndicator()
        } else {
            if (modelsUiState.error != null) {
                ModelsErrorDialog(
                    error=modelsUiState.error,
                    clearError = {})
            } else {
                ModelsListContainer(
                    models = modelsUiState.models,
                    handleEvent = handleEvent
                )
            }
        }
    }
}


@Composable
fun ModelsListContainer(
    models: Array<Model>,
    handleEvent: (event: ModelsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize()
    ) {
        models.map { model ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(R.drawable.models_default_picture),
                        contentDescription = "",
                        modifier = Modifier.width(80.dp).height(80.dp),
                    )
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(text = model.id.toString())
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = model.name)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = model.userStatus.toString())
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = model.imagePath?: "")
                    }
                }
            }
            Spacer(modifier = Modifier.height(1.dp))
        }

    }
}

@Composable
fun ModelsErrorDialog(
    modifier: Modifier = Modifier,
    error: String,
    clearError: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            clearError()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    clearError()
                }
            ) {
                Text(text = stringResource(R.string.login_error_dialog_ok))
            }
        },
        title = {
            Text(
                text= stringResource(R.string.login_error_dialog_title),
                fontSize = 18.sp
            )
        },
        text = {
            Text(error)
        }
    )
}


@Composable
@Preview
fun ModelsScreenPreview() {
    val model1 = Model(
        id = 1,
        name = "test 1",
        imagePath = "path to img",
        userStatus = 5
    )
    val model2 = Model(
        id = 1,
        name = "test 1",
        imagePath = "path to img",
        userStatus = 5
    )
    val model3 = Model(
        id = 1,
        name = "test 1",
        imagePath = "path to img",
        userStatus = 5
    )
    val state = ModelsUiState(
        isLoading = false,
        models = arrayOf(model1,model2,model3)
    )
    WipMobileTheme {
        ModelsScreen(
            modelsUiState = state,
            handleEvent = {}
        )
    }
}