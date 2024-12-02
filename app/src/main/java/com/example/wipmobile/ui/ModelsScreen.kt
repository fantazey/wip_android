package com.example.wipmobile.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wipmobile.ui.models.ModelsUiState
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.ui.components.ErrorDialog
import com.example.wipmobile.ui.models.ModelsEvent
import com.example.wipmobile.ui.models.ModelsListContainer
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            if (modelsUiState.error != null) {
                ErrorDialog(
                    error=modelsUiState.error,
                    clearError = {}
                )
            } else {
                ModelsListContainer(
                    modelResponses = modelsUiState.modelResponses,
                    handleEvent = handleEvent
                )
            }
        }
    }
}

@Composable
@Preview
fun ModelsScreenPreview() {
    val model1 = Model(
        id = 1,
        name = "test 1",
        statusId = 1,
        statusName = "test status1",
        lastImagePath = null,
        hoursSpent = 90.1,

        battleScribeUnitId = 1,
        battleScribeUnitName = "BattleSribe badge text",

        killTeamId = 1,
        killTeamName = "KT Badge text",
        groups = listOf(ModelGroup(id=1,name=" группа 1"), ModelGroup(id=2,name=" группа 2")),
        isTerrain = true,
        unitCount = 180
    )
    val model2 = Model(
        id = 1,
        name = "test 1",
        statusId = 1,
        statusName = "test status2",
        lastImagePath = "path to img",
        hoursSpent = 90.1,

        battleScribeUnitId = 1,
        battleScribeUnitName = "BattleSribe badge text",

        killTeamId = 1,
        killTeamName = "KT Badge text",
        groups = listOf(ModelGroup(id=1,name=" группа 1"), ModelGroup(id=2,name=" группа 2")),
        isTerrain = true,
        unitCount = 180
    )
    val model3 = Model(
        id = 1,
        name = "test 1",
        statusId = 1,
        statusName = "test status3",
        lastImagePath = null,
        hoursSpent = 90.1,

        battleScribeUnitId = 1,
        battleScribeUnitName = "BattleSribe badge text",

        killTeamId = 1,
        killTeamName = "KT Badge text",
        groups = listOf(ModelGroup(id=1,name=" группа 1"), ModelGroup(id=2,name=" группа 2")),
        isTerrain = true,
        unitCount = 180
    )
    val state = ModelsUiState(
        isLoading = false,
        modelResponses = arrayOf(model1, model2, model3)
    )
    WipMobileTheme {
        ModelsScreen(
            modelsUiState = state,
            handleEvent = {}
        )
    }
}
