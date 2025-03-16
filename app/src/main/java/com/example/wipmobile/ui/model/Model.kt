package com.example.wipmobile.ui.model

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.model.UserStatus
import com.example.wipmobile.ui.components.ModelHoursSpend
import com.example.wipmobile.ui.components.ModelImage
import com.example.wipmobile.ui.components.ModelStatus
import com.example.wipmobile.ui.models.ModelData
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

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
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp
                )
        ) {
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
        status = UserStatus(id = 1, name = "test status1"),
        lastImagePath = null,
        isTerrain = false,
        hoursSpent = 10.2,
        unitCount = 10,
        groups = listOf(ModelGroup(id = 1, name = "text")),
        battleScribeUnit = BattleScribeUnit(id = 1, name = "text"),
        killTeam = KillTeam(id = 1, "text"),
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
        status = UserStatus(id = 1, name = "test status1"),
        lastImagePath = null,
        isTerrain = false,
        hoursSpent = 10.2,
        unitCount = 10,
        groups = listOf(ModelGroup(id = 1, name = "text")),
        battleScribeUnit = BattleScribeUnit(id = 1, name = "text"),
        killTeam = KillTeam(id = 1, "text"),
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
        items(progress) { progressLog: ModelProgress ->
            ModelWorkLogItem(progressLog)
        }
    }
}

@Composable
@Preview
fun ModelWorkLogPreview() {
    val p1 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er",
        description = "long long long long long long long long long long long long long long long long long long",
        time = 4.21,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    val p2 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er",
        description = "long long long long long long long long long long long long long long long long long long",
        time = 4.21,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    val p3 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er",
        description = "long long long long long long long long long long long long long long long long long long",
        time = 4.21,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    ModelWorkLog(listOf(p1, p2, p3))
}

@Composable
fun ModelWorkLogItem(progressLog: ModelProgress) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = {}
    ) {
        Row(modifier = Modifier.padding(5.dp)) {
            Column(modifier = Modifier.padding(5.dp)) {
                Text(text = progressLog.title, textAlign = TextAlign.Left)
                Spacer(modifier = Modifier.height(2.dp))
                ModelHoursSpend(progressLog.time)
                Spacer(modifier = Modifier.height(2.dp))
                ModelStatus(progressLog.status.name)
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = progressLog.description ?: "", textAlign = TextAlign.Left)
                Spacer(modifier = Modifier.height(2.dp))
            }
            ModelImage(progressLog.imagePath, false)
        }

    }
}

@Composable
@Preview
fun ModelWorkLogItemPreview() {
    val p3 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er",
        description = "long long long long long long long long long long long long long long long long long long",
        time = 4.21,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    ModelWorkLogItem(p3)
}


@Composable
fun ModelPictures(images: List<ModelImage>) {
    var dialogVisible by remember { mutableStateOf(false) }
    var selectedImagePath: String? by remember { mutableStateOf(null) }
    Box() {
        if (dialogVisible) {
            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { dialogVisible = false },
            ) {
                if (null != selectedImagePath) {
                    ModelImageDialog(
                        imagePath = selectedImagePath!!,
                        onClick = { dialogVisible = false })
                } else {
                    dialogVisible = false
                }
            }
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
            items(images) { image ->
                ModelImage(image.imagePath, false, modifier = Modifier.clickable {
                    dialogVisible = true
                    selectedImagePath = image.imagePath!!
                })
            }
        }
    }
}

@Composable
fun ModelImageDialog(imagePath: String, onClick: () -> Unit) {
    val onClick2 = {
        Log.i("model component", "click close")
        onClick()
    }
    Box(contentAlignment = Alignment.TopEnd) {
        ZoomableImage(imagePath)
        Box(modifier = Modifier.padding(0.dp, 20.dp, 20.dp, 0.dp)) {
            IconButton(onClick = onClick2) {
                Icon(
                    contentDescription = "",
                    imageVector = Icons.Default.Close,
                    modifier = Modifier.background(color = Color.White)
                )
            }
        }
    }
}

@Composable
@Preview
fun ModelImageDialogPreview() {
    return ModelImageDialog("asdasd", {})
}

@SuppressLint("RememberReturnType")
@OptIn(ExperimentalGlideComposeApi::class, ExperimentalFoundationApi::class)
@Composable
fun ZoomableImage(path: String) {
    val angle by remember { mutableStateOf(0f) }
    var zoom by remember { mutableStateOf(1f) }
    var offsetX by remember { mutableStateOf(0f) }
    var offsetY by remember { mutableStateOf(0f) }
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp.value
    val screenHeight = configuration.screenHeightDp.dp.value

    GlideImage(
        model = path,
        contentDescription = "",
        contentScale = ContentScale.Fit,
        modifier = Modifier
            .offset { IntOffset(offsetX.roundToInt(), offsetY.roundToInt()) }
            .graphicsLayer(
                scaleX = zoom,
                scaleY = zoom,
                rotationZ = angle
            )
            .pointerInput(Unit) {
                detectTransformGestures(
                    onGesture = { _, pan, gestureZoom, _ ->
                        zoom = (zoom * gestureZoom).coerceIn(1F..4F)
                        if (zoom > 1) {
                            val x = (pan.x * zoom)
                            val y = (pan.y * zoom)
                            val angleRad = angle * PI / 180.0

                            offsetX =
                                (offsetX + (x * cos(angleRad) - y * sin(angleRad)).toFloat()).coerceIn(
                                    -(screenWidth * zoom)..(screenWidth * zoom)
                                )
                            offsetY =
                                (offsetY + (x * sin(angleRad) + y * cos(angleRad)).toFloat()).coerceIn(
                                    -(screenHeight * zoom)..(screenHeight * zoom)
                                )
                        } else {
                            offsetX = 0F
                            offsetY = 0F
                        }
                    }
                )
            }
            .fillMaxSize()
    )
}