package com.example.wipmobile.ui.models

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus
import com.example.wipmobile.ui.components.ModelHoursSpend
import com.example.wipmobile.ui.components.ModelImage
import com.example.wipmobile.ui.components.ModelStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelsListContainer(
    uiState: ModelsUiState,
    handleEvent: (event: ModelsEvent) -> Unit,
    selectModel: (model: Model) -> Unit,
    modifier: Modifier = Modifier
) {
    var filterPanelVisible by remember { mutableStateOf(false) }
    PullToRefreshBox(
        isRefreshing = uiState.isLoading,
        onRefresh = {
            handleEvent(ModelsEvent.RefreshList)
        }
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FilterChip(
                onClick = { filterPanelVisible = !filterPanelVisible },
                selected = filterPanelVisible,
                label = { Text("Фильтры") },
            )
            if (filterPanelVisible) {
                ModelsFilterPanel(uiState, handleEvent, modifier)
            }
            LazyColumn(
                modifier = modifier.fillMaxSize()
            ) {
                items(uiState.models.size) { index: Int ->
                    ModelCard(uiState.models[index], selectModel = selectModel)
                }
            }
        }
    }
}

@Composable
fun ModelCard(model: Model, selectModel: (model: Model) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { selectModel(model) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(5.dp)
        ) {
            ModelImage(model.lastImagePath)
            ModelData(
                model, modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(5.dp, 0.dp))
            )
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
        groups = listOf(
            ModelGroup(id = 1, name = " группа 1"),
            ModelGroup(id = 2, name = " группа 2")
        ),
        isTerrain = true,
        unitCount = 180
    )
    ModelCard(model = model, selectModel = {})
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
        Text(text = "KillTeam:", fontSize = 8.sp)
        Text(text = name, fontSize = 8.sp)
    }
}

@Composable
fun BattleScribeBadge(name: String) {
    Row {
        Text(text = "BattleScribe:", fontSize = 8.sp)
        Text(text = name, fontSize = 8.sp)
    }
}

@Composable
fun ModelGroupBadge(group: ModelGroup, modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(3.dp)
            )
            .padding(1.dp, 0.dp)
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
        groups = listOf(
            ModelGroup(id = 1, name = " группа 1"),
            ModelGroup(id = 2, name = " группа 2")
        ),
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
fun ModelsFilterPanel(
    uiState: ModelsUiState,
    handleEvent: (event: ModelsEvent) -> Unit,
    modifier: Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = modifier.align(Alignment.CenterHorizontally)) {
            TextField(
                modifier = modifier,
                value = uiState.nameQuery,
                onValueChange = { newValue ->
                    handleEvent(ModelsEvent.ChangeSearchQuery(newValue))
                },
                label = {
                    Text("Название")
                },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                    )
                },
                trailingIcon = {
                    IconButton(
                        onClick = { handleEvent(ModelsEvent.ChangeSearchQuery("")) }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = null,
                        )
                    }
                }
            )
        }
        Row(modifier = modifier.align(Alignment.CenterHorizontally)) {
            ModelsFilterModelGroup(
                values = uiState.availableGroups,
                selected = uiState.selectedGroups,
                onChange = { values: List<ModelGroup> ->
                    handleEvent(ModelsEvent.ChangeModelGroupFilter(values))
                })
            ModelsFilterStatus(
                values = uiState.availableStatuses,
                selected = uiState.selectedStatuses,
                onChange = { values: List<UserStatus> ->
                    handleEvent(ModelsEvent.ChangeUserStatusFilter(values))
                })
        }
        Row(modifier = modifier.align(Alignment.CenterHorizontally)) {
            TextButton(
                onClick = { handleEvent(ModelsEvent.ApplyFilter) }, modifier = modifier.background(
                    Color.Gray
                )
            ) {
                Text("Применить")
            }
        }
    }
}


@Composable
fun ModelsFilterStatus(
    values: List<UserStatus>,
    selected: List<UserStatus>,
    onChange: (values: List<UserStatus>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isSelected = { item: UserStatus ->
        selected.count { it.id == item.id } > 0
    }
    val onItemClick = { item: UserStatus ->
        if (isSelected(item)) {
            onChange(selected.filter { it.id != item.id })
        } else {
            onChange(selected + item)
        }
    }
    Box() {
        TextButton(onClick = { expanded = !expanded }) {
            Text("Статус")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            values.map { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    leadingIcon = {
                        if (isSelected(item)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = ""
                            )
                        }
                    },
                    onClick = { onItemClick(item) }
                )
            }

        }
    }
}


@Composable
fun ModelsFilterModelGroup(
    values: List<ModelGroup>,
    selected: List<ModelGroup>,
    onChange: (values: List<ModelGroup>) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val isSelected = { item: ModelGroup ->
        selected.count { it.id == item.id } > 0
    }
    val onItemClick = { item: ModelGroup ->
        if (isSelected(item)) {
            onChange(selected.filter { it.id != item.id })
        } else {
            onChange(selected + item)
        }
    }
    Box() {
        TextButton(onClick = { expanded = !expanded }) {
            Text("Группа")
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            values.map { item ->
                DropdownMenuItem(
                    text = { Text(item.name) },
                    leadingIcon = {
                        if (isSelected(item)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = ""
                            )
                        }
                    },
                    onClick = { onItemClick(item) }
                )
            }

        }
    }
}