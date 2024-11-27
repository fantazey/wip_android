package com.example.wipmobile.ui

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.wipmobile.R
import com.example.wipmobile.data.ModelsUiState
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.ui.models.ModelsEvent
import com.example.wipmobile.ui.theme.WipMobileTheme
import kotlin.math.floor
import kotlin.math.truncate

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
                ModelsErrorDialog(
                    error=modelsUiState.error,
                    clearError = {})
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
fun ModelsListContainer(
    modelResponses: Array<Model>,
    handleEvent: (event: ModelsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        modelResponses.map { ModelCard(it) }
    }
}

@Composable
fun ModelCard(model: Model) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp)
        ) {
            ModelImage(model.lastImagePath)
            ModelData(model, modifier = Modifier.fillMaxWidth().padding(PaddingValues(5.dp, 0.dp)))
        }
    }
    Spacer(modifier = Modifier.height(1.dp))
}

@Composable
@Preview
fun ModelCardPreview() {
    val model = Model(
        id = 1,
        name = "Chaos Space Marines: Fabius Bile",
        statusId = 1,
        statusName = "Загрунтовано",

        lastImagePath = null,
        hoursSpent = 90.52,

        battleScribeUnitId = 1,
        battleScribeUnitName = "BattleSribe badge text",

        killTeamId = 1,
        killTeamName = "KT Badge text",
        groups = listOf(ModelGroup(id=1,name=" группа 1"), ModelGroup(id=2,name=" группа 2")),
        isTerrain = true,
        unitCount = 180
    )
    ModelCard(model=model)
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun ModelImage(path: String?, modifier: Modifier = Modifier) {
    if (path != null) {
        GlideImage(
            model=path,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier.width(100.dp).height(100.dp).clip(RoundedCornerShape(16.dp)),
        )
    } else {
        Image(
            painter = painterResource(R.drawable.models_default_picture),
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = modifier.width(100.dp).height(100.dp).clip(RoundedCornerShape(16.dp)),
        )
    }
}

@Composable
fun ModelData(model: Model, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(2.dp))
        ModelName(model.name)
        Spacer(modifier = Modifier.height(5.dp))
        ModelStatus(model.statusName)
        Spacer(modifier = Modifier.height(5.dp))
        ModelHoursSpend(model.hoursSpent)
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            model.groups.map {
                ModelGroupBadge(it)
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        if (null != model.killTeamName) {
            Spacer(modifier = Modifier.height(2.dp))
            KillTeamBadge(model.killTeamName)
            Spacer(modifier = Modifier.height(2.dp))
        }
        if (null != model.battleScribeUnitName) {
            Spacer(modifier = Modifier.height(2.dp))
            BattleScribeBadge(model.battleScribeUnitName)
            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}


@Composable
fun KillTeamBadge(name: String) {
    Row {
        Text(text = "KillTeam:", fontSize = 8.sp,)
        Text(text = name, fontSize = 8.sp)
    }
}

@Composable
fun BattleScribeBadge(name: String) {
    Row {
        Text(text = "BattleScribe:", fontSize = 8.sp,)
        Text(text = name, fontSize = 8.sp)
    }
}

@Composable
fun ModelGroupBadge(group: ModelGroup, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier.border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(3.dp)).padding(1.dp, 0.dp)
    ) {
        Spacer(modifier = Modifier.width(2.dp))
        Text(
            text = group.name,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = modifier.padding(0.dp),
        )
    }
}

@Composable
@Preview
fun ModelDataPreview() {
    val model = Model(
        id = 1,
        name = "Chaos Space Marines: Fabius Bile",
        statusId = 1,
        statusName = "Загрунтовано",

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
    ModelData(model = model)
}



@Composable
fun ModelName(name: String, modifier: Modifier = Modifier) {
    Text(text = name, modifier = modifier)
}

@Composable
fun ModelStatus(statusName: String?, modifier: Modifier = Modifier) {
    val name = statusName?: "Статус не определен"
    Box(
        modifier = Modifier.background(color=Color.LightGray, shape = RoundedCornerShape(10.dp)).padding(5.dp)
    ) {
        Text(
            text = name,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = modifier
        )
    }
}

@Composable
fun ModelHoursSpend(hours: Double) {
    val _hours = truncate(hours)
    val humanHours = truncate(_hours).toInt().toString()
    val humanMinutes = truncate((hours - _hours) * 60).toInt().toString()
    return Column {
        Text(text="Времени записано:", fontSize = 8.sp)
        Text(text="$humanHours ч. $humanMinutes м", fontSize = 12.sp)
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
