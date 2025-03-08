package com.example.wipmobile.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
    uiState: ModelsUiState,
    handleEvent: (event: ModelsEvent) -> Unit,
    selectModel: (model: Model) -> Unit
) {
    Log.i("model screen", "Рисуем список моделей")
    if (uiState.models.isEmpty() && !uiState.isLoading) {
        Log.i("model screen", "Список пустой, надо загрузить")
        handleEvent(ModelsEvent.Load)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (uiState.error != null) {
            ErrorDialog(
                error = uiState.error,
                clearError = { handleEvent(ModelsEvent.ClearError) }
            )
        } else {
            ModelsListContainer(
                uiState = uiState,
                handleEvent = handleEvent,
                selectModel = selectModel
            )
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
        groups = listOf(
            ModelGroup(id = 1, name = " группа 1"),
            ModelGroup(id = 2, name = " группа 2")
        ),
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
        groups = listOf(
            ModelGroup(id = 1, name = " группа 1"),
            ModelGroup(id = 2, name = " группа 2")
        ),
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
        groups = listOf(
            ModelGroup(id = 1, name = " группа 1"),
            ModelGroup(id = 2, name = " группа 2")
        ),
        isTerrain = true,
        unitCount = 180
    )
    val state = ModelsUiState(
        isLoading = false,
        models = listOf(model1, model2, model3)
    )
    WipMobileTheme {
        ModelsScreen(
            uiState = state,
            handleEvent = {},
            selectModel = {}
        )
    }
}
