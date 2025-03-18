package com.example.wipmobile.ui.add_model

import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wipmobile.R
import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.model.BattleScribeCategory
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus


@Composable
fun AddModelFormContainer(
    uiState: AddModelUiState,
    handleEvent: (e: AddModelEvent) -> Unit,
    successCallback: (model: Model) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val validateFormAndSave = { formData: ModelFormData ->
        val errors = mutableListOf<String>()
        if (formData.name.isEmpty()) {
            errors.add("Название не заполнено")
        }
        if (formData.unitCount == 0) {
            errors.add("Количество юнитов должно быть больше 0")
        }
        if (null == formData.status) {
            errors.add("Статус должен быть выбран")
        }
        if (errors.isEmpty()) {
            handleEvent(
                AddModelEvent.SaveModel(
                    formData = formData,
                    successCallback = successCallback,
                    errorCallback = {})
            )
        } else {
            Toast.makeText(context, errors.joinToString(","), Toast.LENGTH_LONG).show()
        }
    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AddModelTitle()
        Spacer(modifier = Modifier.height(32.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp, 0.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            ModelForm(
                init = ModelFormData(),
                userStatuses = uiState.userStatuses,
                modelGroups = uiState.modelGroups,
                killTeams = uiState.killTeams,
                battleScribeCategories = uiState.battleScribeCategories,
                battleScribeUnits = uiState.battleScribeUnits,
                saveCallback = validateFormAndSave
            )
        }
    }
}

@Composable
fun ModelForm(
    init: ModelFormData,
    userStatuses: List<UserStatus>,
    killTeams: List<KillTeam>,
    modelGroups: List<ModelGroup>,
    battleScribeCategories: List<BattleScribeCategory>,
    battleScribeUnits: List<BattleScribeUnit>,
    saveCallback: (e: ModelFormData) -> Unit,
    modifier: Modifier = Modifier,
) {

    var name: String by remember { mutableStateOf(init.name) }
    var unitCount: Int by remember { mutableIntStateOf(init.unitCount) }
    var terrain: Boolean by remember { mutableStateOf(init.terrain) }
    var status: UserStatus? by remember { mutableStateOf(init.status) }
    val groups = remember { mutableStateListOf<ModelGroup>().apply { addAll(init.groups) } }
    var battleScribeUnit: BattleScribeUnit? by remember { mutableStateOf(init.battleScribeUnit) }
    val initCategory = if (init.battleScribeUnit?.category != null) {
        battleScribeCategories.first { it.id == init.battleScribeUnit.category.id }
    } else {
        null
    }
    var battleScribeCategory: BattleScribeCategory? by remember { mutableStateOf(initCategory) }
    var killTeam: KillTeam? by remember { mutableStateOf(null) }

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
                onValueChange = { newVal ->
                    name = newVal
                },
                singleLine = true,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Количество юнитов")
            Spacer(modifier = modifier.width(10.dp))
            BasicTextField(
                modifier = modifier
                    .border(width = 1.dp, color = Color.DarkGray)
                    .fillMaxWidth(),
                value = unitCount.toString(),
                onValueChange = { newVal ->
                    unitCount = try {
                        newVal.toInt()
                    } catch (e: Exception) {
                        1
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                ),
            )
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
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Террейн")
            Spacer(modifier = modifier.width(10.dp))
            Checkbox(
                checked = terrain,
                onCheckedChange = { newValue ->
                    terrain = newValue
                }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Килл тим")
            Spacer(modifier = modifier.width(10.dp))
            CommonDropDown<KillTeam>(
                items = killTeams,
                selected = killTeam,
                onChange = { newValue -> killTeam = newValue },
                getLabel = { newValue: KillTeam? -> newValue?.name ?: "Выбрать" }
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Категория BS")
            Spacer(modifier = modifier.width(10.dp))
            CommonDropDown<BattleScribeCategory>(
                items = battleScribeCategories,
                selected = battleScribeCategory,
                onChange = { newValue -> battleScribeCategory = newValue },
                getLabel = { newValue: BattleScribeCategory? -> newValue?.name ?: "Выбрать" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Юнит BS")
            Spacer(modifier = modifier.width(10.dp))
            CommonDropDown<BattleScribeUnit>(
                items = if (battleScribeCategory != null) {
                    battleScribeUnits.filter { it.category != null && it.category.id == battleScribeCategory!!.id }
                } else {
                    battleScribeUnits
                },
                selected = battleScribeUnit,
                onChange = { newValue ->
                    battleScribeUnit = newValue
                    name = newValue?.name ?: ""
                },
                getLabel = { newValue: BattleScribeUnit? -> newValue?.name ?: "Выбрать" }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Absolute.Left
        ) {
            Text("Группы")
            Spacer(modifier = modifier.width(10.dp))
            ModelGroupDropDown(
                items = modelGroups,
                selectedValues = groups,
                onChange = { selected: ModelGroup ->
                    if (groups.contains(selected)) {
                        groups.remove(selected)
                    } else {
                        groups.add(selected)
                    }
                }
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        AddModelSaveButton(onClick = {
            saveCallback(
                ModelFormData(
                    name = name,
                    groups = groups,
                    terrain = terrain,
                    status = status,
                    battleScribeUnit = battleScribeUnit,
                    killTeam = killTeam,
                    unitCount = unitCount
                )
            )
        })
    }
}


@Composable
fun AddModelTitle() {
    Text(
        text = stringResource(R.string.title_activity_add_model),
        fontSize = 24.sp,
        fontWeight = FontWeight.Black,
    )
}

@Composable
fun <T> CommonDropDown(
    items: List<T>,
    selected: T? = null,
    onChange: (newVal: T?) -> Unit,
    getLabel: (value: T?) -> String
) {
    var label: String by remember { mutableStateOf(getLabel(selected)) }
    var menuOpen: Boolean by remember { mutableStateOf(false) }
    val onSelect = { newValue: T ->
        if (selected == newValue || newValue == null) {
            label = "Выбрать"
            onChange(null)
        } else {
            label = getLabel(newValue)
            onChange(newValue)
        }
    }
    Box() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(3.dp)
                .clickable { menuOpen = true }
                .border(width = 1.dp, Color.DarkGray)
        ) {
            Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Icon(contentDescription = "", imageVector = Icons.Default.ArrowDropDown)
        }
        DropdownMenu(
            expanded = menuOpen,
            modifier = Modifier.requiredSizeIn(maxHeight = 300.dp),
            onDismissRequest = { menuOpen = false }
        ) {
            items.map { item ->
                DropdownMenuItem(
                    text = { Text(text = getLabel(item)) },
                    onClick = {
                        onSelect(item)
                        menuOpen = false
                    },
                    trailingIcon = {
                        if (null != selected && selected == item) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Выбран"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun ModelGroupDropDown(
    items: List<ModelGroup>,
    selectedValues: List<ModelGroup>,
    onChange: (newVal: ModelGroup) -> Unit
) {

    val label = selectedValues.joinToString(",") { it.name }
    var menuOpen: Boolean by remember { mutableStateOf(false) }
    val onSelect = { newValue: ModelGroup ->
        onChange(newValue)
    }
    Box() {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .padding(3.dp)
                .clickable { menuOpen = true }
                .border(width = 1.dp, Color.DarkGray)
        ) {
            Text(label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            Icon(contentDescription = "", imageVector = Icons.Default.ArrowDropDown)
        }
        DropdownMenu(
            expanded = menuOpen,
            modifier = Modifier.requiredSizeIn(maxHeight = 300.dp),
            onDismissRequest = { menuOpen = false }
        ) {
            items.map { item ->
                DropdownMenuItem(
                    text = { Text(text = item.name) },
                    onClick = {
                        onSelect(item)
                        menuOpen = false
                    },
                    trailingIcon = {
                        if (selectedValues.contains(item)) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Выбран"
                            )
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun AddModelSaveButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier,
        enabled = true,
        onClick = onClick
    ) {
        Text(text = stringResource(R.string.save_new_model_button_label))
    }
}

@Composable
@Preview
fun AddModelFormPreview() {
    val uiState = AddModelUiState()
    AddModelFormContainer(
        uiState = uiState,
        handleEvent = {},
        successCallback = {}
    )
}