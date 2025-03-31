package com.example.wipmobile.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.wipmobile.data.dto.ModelGroupFormData
import com.example.wipmobile.data.dto.StatusFormData
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus
import com.example.wipmobile.ui.add_model.AddModelSaveButton
import com.example.wipmobile.ui.add_model.CommonDropDown
import com.example.wipmobile.ui.profile.ProfileEvent
import com.example.wipmobile.ui.profile.ProfileUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    handleEvent: (e: ProfileEvent) -> Unit,
    logout: () -> Unit
) {
    if (!uiState.loaded && !uiState.isLoading) {
        Log.i("model screen", "Список пустой, надо загрузить")
        handleEvent(ProfileEvent.LoadData)
    }
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = arrayOf("Профиль", "Статусы", "Группы моделей")
    val createStatusCallback = { form: StatusFormData, cb: () -> Unit ->
        handleEvent(ProfileEvent.CreateStatus(form, cb))
    }
    val updateStatusCallback = { form: StatusFormData, status: UserStatus, cb: () -> Unit ->
        handleEvent(ProfileEvent.UpdateStatus(status, form, cb))
    }

    val createModelGroupCallback = { form: ModelGroupFormData, cb: () -> Unit ->
        handleEvent(ProfileEvent.CreateModelGroup(form, cb))
    }
    val updateModelGroupCallback = { form: ModelGroupFormData, group: ModelGroup, cb: () -> Unit ->
        handleEvent(ProfileEvent.UpdateModelGroup(group, form, cb))
    }
    Column(modifier = Modifier.fillMaxSize()) {
        PrimaryTabRow(
            selectedTabIndex = selectedTab,
            modifier = Modifier.border(
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
                Column {
                    Text(text = "Профиль пользователя")
                    Button(onClick = logout) {
                        Text("Выход")
                    }
                }
            }

            1 -> {
                StatusBlock(
                    statuses = uiState.statuses,
                    createCallback = createStatusCallback,
                    updateCallback = updateStatusCallback
                )
            }

            2 -> {
                ModelGroupBlock(
                    groups = uiState.groups,
                    createCallback = createModelGroupCallback,
                    updateCallback = updateModelGroupCallback
                )
            }
        }

    }
}


@Composable
fun StatusBlock(
    statuses: List<UserStatus>,
    createCallback: (e: StatusFormData, cb: () -> Unit) -> Unit,
    updateCallback: (e: StatusFormData, status: UserStatus, cb: () -> Unit) -> Unit,
) {
    var showAddStatusForm by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.padding(3.dp)) {
        item {
            TextButton(onClick = { showAddStatusForm = !showAddStatusForm }) {
                Text("Добавить статус")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            if (showAddStatusForm) {
                StatusForm(
                    init = StatusFormData(),
                    userStatuses = statuses,
                    saveCallback = { form -> createCallback(form) { showAddStatusForm = false } },
                    cancelEditCallback = { showAddStatusForm = false })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
        items(statuses) { item: UserStatus ->
            StatusCard(item, statuses, updateCallback)
        }
    }
}

@Composable
fun StatusCard(
    status: UserStatus,
    statusList: List<UserStatus>,
    updateCallback: (e: StatusFormData, status: UserStatus, cb: () -> Unit) -> Unit,
) {
    val prev: UserStatus? = statusList.find { it.id == status.previous }
    val next: UserStatus? = statusList.find { it.id == status.next }

    val boolToString = { value: Boolean -> if (value) "Да" else "Нет" }
    var showEditForm by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        if (showEditForm) {
            StatusForm(
                init = StatusFormData(
                    id = status.id,
                    name = status.name,
                    order = status.order,
                    previous = prev,
                    next = next,
                    isInitial = status.isInitial,
                    isFinal = status.isFinal
                ),
                userStatuses = statusList,
                cancelEditCallback = { showEditForm = false },
                saveCallback = { form ->
                    updateCallback(form, status) { showEditForm = false }
                }
            )
        } else {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { showEditForm = true }) {
                        Icon(contentDescription = "", imageVector = Icons.Default.Edit)
                    }
                }
                Text(text = "Название: ${status.name}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Порядок сортировки: ${status.order}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Предыдущий: ${prev?.name ?: "Не указан"}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Следующий: ${next?.name ?: "Не указан"}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Начальный: ${boolToString(status.isInitial)}")
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Финальный: ${boolToString(status.isFinal)}")
            }

        }
    }
}


@Composable
fun StatusForm(
    init: StatusFormData,
    userStatuses: List<UserStatus>,
    saveCallback: (e: StatusFormData) -> Unit,
    cancelEditCallback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name: String by remember { mutableStateOf(init.name) }
    var order: Int by remember { mutableIntStateOf(init.order) }
    var nextStatus: UserStatus? by remember { mutableStateOf(init.next) }
    var previousStatus: UserStatus? by remember { mutableStateOf(init.previous) }
    var isInitial: Boolean by remember { mutableStateOf(init.isInitial) }
    var isFinal: Boolean by remember { mutableStateOf(init.isFinal) }

    val context = LocalContext.current
    val validateAndSave = {
        val errors = mutableListOf<String>()
        if (name.isEmpty()) {
            errors.add("Название должно быть указано")
        }
        if (errors.isEmpty()) {
            val formData = StatusFormData(
                id = init.id,
                name = name,
                order = order,
                next = nextStatus,
                previous = previousStatus,
                isInitial = isInitial,
                isFinal = isFinal
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
            Text("Название")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray)
                    .fillMaxWidth(),
                value = name,
                onValueChange = { newVal -> name = newVal },
                singleLine = true,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Порядок сортировки")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray),
                value = order.toString(),
                onValueChange = { newVal ->
                    order = try {
                        newVal.toInt()
                    } catch (e: Exception) {
                        0
                    }
                },
                singleLine = false,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Предыдущий статус")
            Spacer(modifier = modifier.width(10.dp))
            CommonDropDown<UserStatus>(
                items = userStatuses,
                selected = previousStatus,
                onChange = { newValue -> previousStatus = newValue },
                getLabel = { newValue: UserStatus? -> newValue?.name ?: "Выбрать" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Следующий статус")
            Spacer(modifier = modifier.width(10.dp))
            CommonDropDown<UserStatus>(
                items = userStatuses,
                selected = nextStatus,
                onChange = { newValue -> nextStatus = newValue },
                getLabel = { newValue: UserStatus? -> newValue?.name ?: "Выбрать" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Начальный статус")
            Spacer(modifier = modifier.width(10.dp))
            Checkbox(
                checked = isInitial,
                onCheckedChange = { newValue ->
                    isInitial = newValue
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Финальный статус")
            Spacer(modifier = modifier.width(10.dp))
            Checkbox(
                checked = isFinal,
                onCheckedChange = { newValue ->
                    isFinal = newValue
                }
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


@Composable
fun ModelGroupBlock(
    groups: List<ModelGroup>,
    createCallback: (e: ModelGroupFormData, cb: () -> Unit) -> Unit,
    updateCallback: (e: ModelGroupFormData, status: ModelGroup, cb: () -> Unit) -> Unit,
) {
    var showAddForm by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.padding(3.dp)) {
        item {
            TextButton(onClick = { showAddForm = !showAddForm }) {
                Text("Добавить группу")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        item {
            if (showAddForm) {
                ModelGroupForm(
                    init = ModelGroupFormData(),
                    saveCallback = { form -> createCallback(form) { showAddForm = false } },
                    cancelEditCallback = { showAddForm = false })
                Spacer(modifier = Modifier.height(10.dp))
            }
        }

        items(groups) { item: ModelGroup ->
            ModelGroupCard(item, updateCallback)
        }
    }
}

@Composable
fun ModelGroupCard(
    group: ModelGroup,
    updateCallback: (e: ModelGroupFormData, status: ModelGroup, cb: () -> Unit) -> Unit
) {
    var showEditForm by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
    ) {
        if (showEditForm) {
            ModelGroupForm(
                init = ModelGroupFormData(name = group.name),
                cancelEditCallback = { showEditForm = false },
                saveCallback = {form: ModelGroupFormData -> updateCallback(form, group) {showEditForm = false} }
            )
        } else {
            Column(modifier = Modifier.padding(10.dp)) {
                Row(horizontalArrangement = Arrangement.End) {
                    IconButton(onClick = { showEditForm = true }) {
                        Icon(contentDescription = "", imageVector = Icons.Default.Edit)
                    }
                }
                Text(text = "Название: ${group.name}")
            }

        }
    }
}

@Composable
fun ModelGroupForm(
    init: ModelGroupFormData,
    saveCallback: (e: ModelGroupFormData) -> Unit,
    cancelEditCallback: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var name: String by remember { mutableStateOf(init.name) }
    val context = LocalContext.current
    val validateAndSave = {
        val errors = mutableListOf<String>()
        if (name.isEmpty()) {
            errors.add("Название должно быть указано")
        }
        if (errors.isEmpty()) {
            val formData = ModelGroupFormData(name = name)
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
            Text("Название")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray)
                    .fillMaxWidth(),
                value = name,
                onValueChange = { newVal -> name = newVal },
                singleLine = true,
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