package com.example.wipmobile.ui.models

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.ui.components.ModelHoursSpend
import com.example.wipmobile.ui.components.ModelImage
import com.example.wipmobile.ui.components.ModelStatus

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