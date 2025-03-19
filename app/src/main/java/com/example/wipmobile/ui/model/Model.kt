package com.example.wipmobile.ui.model

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.dto.ModelProgressFormData
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.model.UserStatus
import com.example.wipmobile.ui.add_model.AddModelSaveButton
import com.example.wipmobile.ui.add_model.CommonDropDown
import com.example.wipmobile.ui.add_model.ModelForm
import com.example.wipmobile.ui.components.ModelHoursSpend
import com.example.wipmobile.ui.components.ModelImage
import com.example.wipmobile.ui.components.ModelStatus
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Objects
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.truncate

@RequiresApi(Build.VERSION_CODES.P)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelCard(
    uiState: ModelUiState,
    handleEvent: (e: ModelEvent) -> Unit,
    navigateBackCallback: () -> Unit,
    navigateToProgressCallback: () -> Unit,
    modifier: Modifier = Modifier
) {
    val model: Model = uiState.model!!
    val progress: List<ModelProgress> = uiState.progress
    val images: List<ModelImage> = uiState.images
    var selectedTab by remember { mutableIntStateOf(uiState.selectedTab) }
    var editMode by remember { mutableStateOf(false) }
    val tabs = arrayOf("Модель", "Лог работ", "Галерея")
    val modelSavedToast =
        Toast.makeText(LocalContext.current, "Модель обновлена", Toast.LENGTH_LONG)
    val successCallback = {
        editMode = false
        modelSavedToast.show()
    }
    val onSelectLog = { selectedModelProgress: ModelProgress ->
        handleEvent(ModelEvent.SelectModelProgress(model, selectedModelProgress))
        navigateToProgressCallback()
    }
    val saveImages = { imagesToUpload: List<Bitmap>, resetCallback: () -> Unit ->
        handleEvent(
            ModelEvent.UploadImages(
                model = model,
                images = imagesToUpload,
                resetCallback = resetCallback
            )
        )
    }
    val saveModelForm = { formData: ModelFormData ->
        handleEvent(
            ModelEvent.UpdateModel(
                model = model,
                data = formData,
                successCallback = successCallback
            )
        )
    }
    val onEditProgressClick = { selectedModelProgress: ModelProgress ->
        handleEvent(ModelEvent.SelectModelProgress(model, selectedModelProgress, true))
        navigateToProgressCallback()
    }

    val onDeleteModelProgress = { selectedModelProgress: ModelProgress ->
        handleEvent(ModelEvent.DeleteModelProgress(model, selectedModelProgress, {}))
    }
    val logTimeCallback = {
        handleEvent(ModelEvent.SelectModelProgress(model = model, openAddProgressForm = true))
        navigateToProgressCallback()
    }
    val refreshCallback = {
        handleEvent(ModelEvent.Refresh)
    }
    val onDeleteImage = { image: ModelImage, callback: () -> Unit ->
        handleEvent(ModelEvent.DeleteImage(model, listOf(image), callback))
    }
    Scaffold(
        topBar = {
            ModelTopBar(
                model = model,
                selectedTab = selectedTab,
                toggleEditCallback = { if (selectedTab == 0) editMode = !editMode },
                navigateBackCallback = navigateBackCallback,
                refreshCallback = refreshCallback,
                logTimeCallback = logTimeCallback
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
                        onClick = {
                            selectedTab = index
                            editMode = false
                        },
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
                    if (editMode) {
                        ModelForm(
                            init = ModelFormData(
                                name = model.name,
                                status = model.status,
                                unitCount = model.unitCount,
                                terrain = model.isTerrain,
                                groups = model.groups,
                                killTeam = model.killTeam,
                                battleScribeUnit = model.battleScribeUnit,
                            ),
                            userStatuses = uiState.userStatuses,
                            modelGroups = uiState.modelGroups,
                            killTeams = uiState.killTeams,
                            battleScribeCategories = uiState.battleScribeCategories,
                            battleScribeUnits = uiState.battleScribeUnits,
                            saveCallback = saveModelForm,
                            cancelEditCallback = { editMode = false }
                        )
                    } else {
                        ModelMain(model)
                    }
                }

                1 -> {
                    ModelWorkLog(
                        progress,
                        onSelectLog = onSelectLog,
                        onEditProgressClick = onEditProgressClick,
                        onDeleteProgressClick = onDeleteModelProgress
                    )
                }


                2 -> {
                    ModelPictures(images, saveImages = saveImages, deleteImage = onDeleteImage)
                }

                else -> {
                    Text("Ошибка")
                }
            }
        }

    }
}

@RequiresApi(Build.VERSION_CODES.P)
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
    val uiState = ModelUiState(
        model = model,
        progress = emptyList(),
        images = emptyList()
    )
    ModelCard(
        uiState = uiState,
        navigateBackCallback = {},
        handleEvent = {},
        navigateToProgressCallback = {})
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelTopBar(
    model: Model,
    selectedTab: Int,
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
            if (selectedTab == 0) {
                IconButton(onClick = toggleEditCallback) {
                    Icon(contentDescription = "", imageVector = Icons.Default.Edit)
                }
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
        refreshCallback = {},
        selectedTab = 0
    )
}

@Composable
fun ModelMain(model: Model) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Название")
            Spacer(modifier = Modifier.width(12.dp))
            Text(model.name)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Количество юнитов")
            Spacer(modifier = Modifier.width(12.dp))
            Text(model.unitCount.toString())
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Статус")
            Spacer(modifier = Modifier.width(12.dp))
            Text(model.status.name)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Группы")
            Spacer(modifier = Modifier.width(12.dp))
            model.groups.map {
                Text(it.name)
                Spacer(modifier = Modifier.width(5.dp))
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Киллтим")
            Spacer(modifier = Modifier.width(12.dp))
            if (null != model.killTeam) {
                Text(model.killTeam.name)
            } else {
                Text("Не указано")
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Юнит BS")
            Spacer(modifier = Modifier.width(12.dp))
            if (null != model.battleScribeUnit) {
                Text(model.battleScribeUnit.name)
            } else {
                Text("Не указано")
            }

        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Террейн")
            Spacer(modifier = Modifier.width(12.dp))
            if (model.isTerrain) {
                Text("Дв")
            } else {
                Text("Нет")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Времени затрачено")
            Spacer(modifier = Modifier.width(12.dp))
            ModelHoursSpend(model.hoursSpent.toFloat())
        }
    }
}


@Composable
fun ModelWorkLog(
    progress: List<ModelProgress>,
    onSelectLog: (selected: ModelProgress) -> Unit,
    onEditProgressClick: (selected: ModelProgress) -> Unit,
    onDeleteProgressClick: (selected: ModelProgress) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(progress) { progressLog: ModelProgress ->
            ModelWorkLogItem(
                progressLog,
                onSelectLog = onSelectLog,
                onEditProgressClick = onEditProgressClick,
                onDeleteProgressClick = onDeleteProgressClick
            )
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
        time = 4.21f,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    val p2 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er",
        description = "long long long long long long long long long long long long long long long long long long",
        time = 4.21f,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    val p3 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er",
        description = "long long long long long long long long long long long long long long long long long long",
        time = 4.21f,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    ModelWorkLog(listOf(p1, p2, p3), {}, {}, {})
}

@Composable
fun ModelWorkLogItem(
    progressLog: ModelProgress,
    onSelectLog: (selected: ModelProgress) -> Unit,
    onEditProgressClick: (selected: ModelProgress) -> Unit,
    onDeleteProgressClick: (selected: ModelProgress) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(2.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        onClick = { onSelectLog(progressLog) }
    ) {
        Box() {
            Row(modifier = Modifier.padding(5.dp)) {
                ModelImage(progressLog.imagePath, true)
                Column(modifier = Modifier.padding(5.dp)) {
                    Text(
                        text = progressLog.title,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.sizeIn(maxWidth = 180.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ModelHoursSpend(progressLog.time)
                        Spacer(modifier = Modifier.width(12.dp))
                        ModelStatus(progressLog.status.name)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(text = progressLog.description ?: "", textAlign = TextAlign.Left)
                }
            }
            Row(modifier = Modifier.align(Alignment.TopEnd)) {
                IconButton(onClick = { onEditProgressClick(progressLog) }) {
                    Icon(contentDescription = "", imageVector = Icons.Default.Edit)
                }
                IconButton(onClick = { onDeleteProgressClick(progressLog) }) {
                    Icon(contentDescription = "", imageVector = Icons.Default.Delete)
                }
            }
        }


    }
}

@Composable
@Preview
fun ModelWorkLogItemPreview() {
    val p3 = ModelProgress(
        id = 1,
        title = "test trest ters tresr tsrts er asd asd asd as asd asd asd asd asd",
        description = "long long long long long long long long long long long long long long long long long long 123 123 12 3123 123 123",
        time = 4.21f,
        status = UserStatus(id = 1, name = "test status1"),
        createdAt = "date date",
        imagePath = null
    )
    ModelWorkLogItem(p3, {}, {}, {})
}


@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ModelPictures(
    images: List<ModelImage>,
    saveImages: (images: List<Bitmap>, resetCallback: () -> Unit) -> Unit,
    deleteImage: (image: ModelImage, callback: () -> Unit) -> Unit
) {
    var dialogVisible by remember { mutableStateOf(false) }
    var selectedImage: ModelImage? by remember { mutableStateOf(null) }
    val deleteImageCallback = {
        dialogVisible = false
        selectedImage = null
    }
    Box() {
        if (dialogVisible) {
            Dialog(
                properties = DialogProperties(usePlatformDefaultWidth = false),
                onDismissRequest = { dialogVisible = false },
            ) {
                if (null != selectedImage) {
                    ModelImageDialog(
                        imagePath = selectedImage!!.imagePath!!,
                        onCloseClick = { dialogVisible = false },
                        onDeleteClick = { deleteImage(selectedImage!!, deleteImageCallback) })
                } else {
                    dialogVisible = false
                }
            }
        }
        LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 128.dp)) {
            item {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .fillMaxHeight()
                        .border(width = 1.dp, color = Color.Black)
                ) {
                    ImagePicker(type = ImagePickerType.Camera, saveImages = saveImages)
                }
            }
            item {
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .fillMaxHeight()
                        .border(width = 1.dp, color = Color.Black)
                ) {
                    ImagePicker(type = ImagePickerType.Gallery, saveImages = saveImages)
                }
            }
            items(images) { image ->
                Box(
                    modifier = Modifier
                        .size(128.dp)
                        .fillMaxHeight()
                        .border(width = 1.dp, color = Color.Black)
                ) {
                    ModelImage(
                        image.imagePath,
                        false,
                        size = 128.dp,
                        modifier = Modifier.clickable {
                            dialogVisible = true
                            selectedImage = image
                        })
                }
            }
        }

    }
}

enum class ImagePickerType {
    Camera, Gallery
}

@RequiresApi(Build.VERSION_CODES.P)
@Composable
fun ImagePicker(
    type: ImagePickerType,
    saveImages: (image: List<Bitmap>, resetCallback: () -> Unit) -> Unit
) {
    val context = LocalContext.current
    var currentPhotoUri by remember { mutableStateOf(value = Uri.EMPTY) }
    val file = context.createImageFile()
    val uri = FileProvider.getUriForFile(
        Objects.requireNonNull(context),
        "${com.example.wipmobile.BuildConfig.APPLICATION_ID}.provider",
        file
    )
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                currentPhotoUri = uri
            }
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { galleryUri: Uri? ->
            if (null != galleryUri && galleryUri.toString().isNotEmpty()) {
                currentPhotoUri = galleryUri
            }
        }
    )
    val resetCallback = {
        currentPhotoUri = Uri.EMPTY
    }
    val onClickHandler = {
        if (currentPhotoUri.toString().isNotEmpty()) {
            val imageResource = ImageDecoder.createSource(context.contentResolver, currentPhotoUri)
            val imageBitmap = ImageDecoder.decodeBitmap(imageResource)
            saveImages(listOf(imageBitmap), resetCallback)
        } else {
            if (type == ImagePickerType.Camera) {
                cameraLauncher.launch(uri)
            } else {
                galleryLauncher.launch("image/*")
            }

        }
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        if (currentPhotoUri.toString().isNotEmpty()) {
            Image(
                bitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        context.contentResolver,
                        currentPhotoUri
                    )
                ).asImageBitmap(),
                contentDescription = "",
                modifier = Modifier.size(128.dp)
            )
        }

        IconButton(
            onClick = onClickHandler,
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize()
        ) {
            if (currentPhotoUri.toString().isNotEmpty()) {
                Icon(contentDescription = "", imageVector = Icons.Default.Save, tint = Color.White)
            } else {
                if (type == ImagePickerType.Camera) {
                    Icon(
                        contentDescription = "",
                        imageVector = Icons.Default.Camera,
                        tint = Color.White
                    )
                }
                if (type == ImagePickerType.Gallery) {
                    Icon(
                        contentDescription = "",
                        imageVector = Icons.Default.Image,
                        tint = Color.White
                    )
                }
            }
        }
    }
}

fun Context.createImageFile(): File {
    // Create an image file name
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
    val imageFileName = "JPEG_" + timeStamp + "_"
    return File.createTempFile(
        imageFileName, /* prefix */
        ".jpg", /* suffix */
        externalCacheDir /* directory */
    )
}

@Composable
fun ModelImageDialog(imagePath: String, onCloseClick: () -> Unit, onDeleteClick: () -> Unit) {
    Box(contentAlignment = Alignment.TopEnd) {
        ZoomableImage(imagePath)
        Box(modifier = Modifier.padding(0.dp, 20.dp, 20.dp, 0.dp)) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                IconButton(onClick = onCloseClick) {
                    Icon(
                        contentDescription = "",
                        imageVector = Icons.Default.Close,
                        modifier = Modifier.background(color = Color.White)
                    )
                }
                IconButton(onClick = onDeleteClick) {
                    Icon(
                        contentDescription = "",
                        imageVector = Icons.Default.Delete,
                        modifier = Modifier.background(color = Color.White)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun ModelImageDialogPreview() {
    return ModelImageDialog("asdasd", {}, {})
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

@Composable
fun ProgressCard(
    uiState: ModelUiState,
    handleEvent: (event: ModelEvent) -> Unit,
    navigateBackCallback: () -> Unit,
) {
    var editMode by remember { mutableStateOf(uiState.openEditProgressForm) }
    var addProgressMode by remember { mutableStateOf(uiState.openAddProgressForm) }
    val saveEditProgressCallback = { formData: ModelProgressFormData ->
        handleEvent(
            ModelEvent.UpdateModelProgress(
                model = uiState.model!!,
                modelProgress = uiState.modelProgress!!,
                data = formData,
                successCallback = {
                    addProgressMode = false
                    editMode = false
                    navigateBackCallback()
                })
        )
    }
    val saveAddProgressCallback = { formData: ModelProgressFormData ->
        handleEvent(
            ModelEvent.CreateModelProgress(
                model = uiState.model!!,
                data = formData,
                successCallback = {
                    addProgressMode = false
                    editMode = false
                    navigateBackCallback()
                })
        )
    }
    val logTimeCallback = {
        editMode = false
        addProgressMode = !addProgressMode
    }
    Scaffold(
        topBar = {
            ProgressTopBar(
                model = uiState.model!!,
                progress = uiState.progress[0],
                toggleEditCallback = {
                    editMode = !editMode
                    addProgressMode = false
                },
                navigateBackCallback = navigateBackCallback,
                refreshCallback = { handleEvent(ModelEvent.Refresh) },
                logTimeCallback = logTimeCallback,
                deleteProgressCallback = {
                    handleEvent(
                        ModelEvent.DeleteModelProgress(
                            uiState.model,
                            uiState.modelProgress!!,
                            navigateBackCallback
                        )
                    )
                    navigateBackCallback()
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LayoutDirection.Ltr),
                    bottom = 0.dp
                )
        ) {
            if (addProgressMode) {
                ModelProgressForm(
                    init = ModelProgressFormData(status = uiState.model!!.status),
                    userStatuses = uiState.userStatuses,
                    saveCallback = saveAddProgressCallback,
                    cancelEditCallback = {
                        addProgressMode = false
                        navigateBackCallback()
                    }
                )
            } else {
                if (uiState.modelProgress == null) {
                    Text("Не выбран лог работ")
                } else {
                    if (editMode) {
                        ModelProgressForm(
                            init = ModelProgressFormData(
                                title = uiState.modelProgress.title,
                                description = uiState.modelProgress.description ?: "",
                                status = uiState.modelProgress.status,
                                time = uiState.modelProgress.time,
                                images = emptyList()
                            ),
                            userStatuses = uiState.userStatuses,
                            saveCallback = saveEditProgressCallback,
                            cancelEditCallback = {
                                editMode = false
                                addProgressMode = false
                            }
                        )
                    } else {
                        ProgressMain(progress = uiState.modelProgress)
                    }
                }

            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgressTopBar(
    progress: ModelProgress,
    model: Model,
    navigateBackCallback: () -> Unit,
    toggleEditCallback: () -> Unit,
    logTimeCallback: () -> Unit,
    refreshCallback: () -> Unit,
    deleteProgressCallback: () -> Unit
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
            IconButton(onClick = deleteProgressCallback) {
                Icon(contentDescription = "", imageVector = Icons.Default.Delete)
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
fun ProgressMain(
    progress: ModelProgress
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Заголовок")
            Spacer(modifier = Modifier.width(12.dp))
            Text(progress.title)
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Описание работ")
            Spacer(modifier = Modifier.width(12.dp))
            Text(progress.description ?: "")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            val hours = truncate(progress.time)
            val decimalMinutes = progress.time - hours
            val minutes = truncate(decimalMinutes * 60)
            Text("Время затрачено")
            Spacer(modifier = Modifier.width(12.dp))
            Text("$hours ч. $minutes м.")
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Статус")
            Spacer(modifier = Modifier.width(12.dp))
            Text(progress.status.name)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Text("Запись от")
            Spacer(modifier = Modifier.width(12.dp))
            Text(progress.createdAt)
        }
    }
}


@Composable
fun ModelProgressForm(
    init: ModelProgressFormData,
    userStatuses: List<UserStatus>,
    saveCallback: (e: ModelProgressFormData) -> Unit,
    cancelEditCallback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var title: String by remember { mutableStateOf(init.title) }
    var description: String by remember { mutableStateOf(init.description) }
    var timeHours: Int by remember { mutableIntStateOf(truncate(init.time).toInt()) }
    var timeMinutes: Int by remember { mutableIntStateOf(truncate((init.time - truncate(init.time)) * 60).toInt()) }
    var status: UserStatus? by remember { mutableStateOf(init.status) }
    val context = LocalContext.current
    val validateAndSave = {
        val errors = mutableListOf<String>()
        if (title.isEmpty()) {
            errors.add("Заголовок должен быть указан")
        }
        if (status == null) {
            errors.add("Статус должен быть указан")
        }
        if (errors.isEmpty()) {
            val formData = ModelProgressFormData(
                title = title,
                description = description,
                time = timeHours + (timeMinutes / 60f),
                status = status,
                images = emptyList()
            )
            saveCallback(formData)
        } else {
            Toast.makeText(context, errors.joinToString(","), Toast.LENGTH_LONG).show()
        }
    }
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Заголовок")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray)
                    .fillMaxWidth(),
                value = title,
                onValueChange = { newVal -> title = newVal },
                singleLine = true,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Описание")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray)
                    .fillMaxWidth(),
                value = description,
                onValueChange = { newVal -> description = newVal },
                singleLine = false
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Затрачено")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray),
                value = timeHours.toString(),
                onValueChange = { newVal ->
                    timeHours = try {
                        newVal.toInt()
                    } catch (e: Exception) {
                        0
                    }
                },
                singleLine = false,
            )
            Text("ч.")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray),
                value = timeMinutes.toString(),
                onValueChange = { newVal ->
                    timeMinutes = try {
                        newVal.toInt()
                    } catch (e: Exception) {
                        0
                    }
                },
                singleLine = false,
            )
            Text("ч.")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Статус")
            Spacer(modifier = modifier.width(10.dp))
            CommonDropDown<UserStatus>(
                items = userStatuses,
                selected = status,
                onChange = { newValue -> status = newValue },
                getLabel = { newValue: UserStatus? -> newValue?.name ?: "Выбрать" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(horizontalArrangement = Arrangement.Absolute.SpaceAround) {
            AddModelSaveButton(onClick = validateAndSave)
            Button(
                modifier = modifier,
                enabled = true,
                onClick = cancelEditCallback
            ) {
                Text("Отмена")
            }
        }
    }
}