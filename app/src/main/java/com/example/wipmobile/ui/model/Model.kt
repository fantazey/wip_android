package com.example.wipmobile.ui.model

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.ui.components.ModelImage
import com.example.wipmobile.ui.models.ModelData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelCard(
    model: Model,
    progress: List<ModelProgress>,
    images: List<ModelImage>,
    handleEvent: (e: ModelEvent) -> Unit,
    navigateBackCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var editMode by remember { mutableStateOf(false) }
    val tabs = arrayOf("Модель", "Лог работ", "Галерея")
    Scaffold(
        topBar = {
            ModelTopBar(
                model = model,
                toggleEditCallback = { editMode = !editMode },
                navigateBackCallback = navigateBackCallback,
                refreshCallback = { handleEvent(ModelEvent.Refresh) },
                logTimeCallback = {}
            )
        }
    ) { innerPadding ->
        Column(modifier = modifier
            .fillMaxWidth()
            .padding(innerPadding)) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab,
                modifier = modifier.border(
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Black
                    ), shape = RoundedCornerShape(5.dp)
                )
            ) {
                tabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = tab,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    )
                }
            }
            when (selectedTab) {
                0 -> {
                    ModelMain(model)
                }

                1 -> {
                    ModelWorkLog(progress)
                }

                2 -> {
                    ModelPictures(images)
                }

                else -> {
                    Text("Ошибка")
                }
            }
        }

    }
}

@Composable
@Preview
fun ModelCardPreview() {
    val model = Model(
        id = 1,
        name = "Long long long long long long long text",
        statusId = 1,
        statusName = "test",
        lastImagePath = null,
        isTerrain = false,
        hoursSpent = 10.2,
        unitCount = 10,
        groups = listOf(ModelGroup(id = 1, name = "text")),
        battleScribeUnitId = 1,
        battleScribeUnitName = "text",
        killTeamId = 1,
        killTeamName = "text"
    )
    ModelCard(
        model = model,
        progress = emptyList(),
        images = emptyList(),
        navigateBackCallback = {},
        handleEvent = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelTopBar(
    model: Model,
    navigateBackCallback: () -> Unit,
    toggleEditCallback: () -> Unit,
    logTimeCallback: () -> Unit,
    refreshCallback: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = model.name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )
        },
        navigationIcon = {
            IconButton(onClick = navigateBackCallback) {
                Icon(contentDescription = "", imageVector = Icons.Default.ArrowBackIosNew)
            }
        },
        actions = {
            IconButton(onClick = toggleEditCallback) {
                Icon(contentDescription = "", imageVector = Icons.Default.Edit)
            }
            IconButton(onClick = logTimeCallback) {
                Icon(contentDescription = "", imageVector = Icons.Default.Timer)
            }
            IconButton(onClick = refreshCallback) {
                Icon(contentDescription = "", imageVector = Icons.Default.Refresh)
            }
        },
        scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(),
        modifier = Modifier.border(
            border = BorderStroke(width = 1.dp, color = Color.Black),
            shape = RoundedCornerShape(5.dp)
        )
    )
}

@Composable
@Preview
fun ModelTopBarPreview() {
    val model = Model(
        id = 1,
        name = "Long long long long long long long text",
        statusId = 1,
        statusName = "test",
        lastImagePath = null,
        isTerrain = false,
        hoursSpent = 10.2,
        unitCount = 10,
        groups = listOf(ModelGroup(id = 1, name = "text")),
        battleScribeUnitId = 1,
        battleScribeUnitName = "text",
        killTeamId = 1,
        killTeamName = "text"
    )
    ModelTopBar(
        model = model,
        navigateBackCallback = {},
        toggleEditCallback = {},
        logTimeCallback = {},
        refreshCallback = {}
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelMain(model: Model) {
    PullToRefreshBox(isRefreshing = false, onRefresh = {}) {
        Column(modifier = Modifier.fillMaxWidth()) {
            ModelData(
                model, modifier = Modifier
                    .fillMaxWidth()
                    .padding(PaddingValues(5.dp, 0.dp))
            )
        }
    }
}


@Composable
fun ModelWorkLog(progress: List<ModelProgress>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        itemsIndexed(progress) { index: Int, progressLog: ModelProgress ->
            Row {
                Text(index.toString())
                Text(progressLog.time.toString())
                Text(progressLog.title)
                Text(progressLog.description)
                Text(progressLog.statusName!!)
                Text(progressLog.createdAt)
                ModelImage(progressLog.imagePath)
            }
        }
    }
}

@Composable
fun ModelPictures(images: List<ModelImage>) {
    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
        items(images) { image ->
            ModelImage(image.imagePath)
        }
    }
}