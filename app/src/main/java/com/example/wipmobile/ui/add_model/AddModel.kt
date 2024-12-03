package com.example.wipmobile.ui.add_model

import androidx.annotation.StringRes
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wipmobile.R
import com.example.wipmobile.ui.components.ModelStatus


@Composable
fun AddModelForm(
    uiState: AddModelUiState,
    eventHandler: (e: AddModelEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        AddModelTitle()
        Spacer(modifier = Modifier.height(40.dp))
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AddModelTextInput(
                    value = uiState.name,
                    label = R.string.add_model_form_name_label,
                    onChanged = { newVal: String ->
                        eventHandler(AddModelEvent.NameChanged(newVal))
                    },
                    onNextClicked = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                AddModelNumberInput(
                    value = uiState.unitCount,
                    label = R.string.add_model_form_count_label,
                    onChanged = { newVal: Int ->
                        eventHandler(AddModelEvent.UnitCountChanged(newVal))
                    },
                    onNextClicked = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                AddModelDropDown(
                    label = R.string.add_model_form_status_label,
                    value = "test",
                    items = arrayOf(
                        "menu item 1",
                        "menu item 2",
                        "menu item 3",
                        "menu item 4",
                        "menu item 5",
                        "menu item 6",
                        "menu item 7",
                        "menu item 8",
                        "menu item 9",
                        "menu item 10",
                        "menu item 11",
                        "menu item 12",
                        "menu item 13",
                        "menu item 14",
                        "menu item 15",
                        "menu item 16",
                        "menu item 17",
                        "menu item 18",
                        "menu item 19",
                    ),
                    onChanged = {}
                )
                Spacer(modifier = Modifier.height(16.dp))
                Spacer(modifier = Modifier.height(16.dp))
                AddModelSaveButton()
            }
        }
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
fun AddModelTextInput(
    modifier: Modifier = Modifier,
    value: String,
    @StringRes label: Int,
    onChanged: (newVal: String) -> Unit,
    onNextClicked: () -> Unit
) {
    TextField(
        modifier = modifier,
        value = value,
        onValueChange = { newVal ->
            onChanged(newVal)
        },
        label = {
            Text(stringResource(label))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNextClicked() }
        )
    )
}


@Composable
fun AddModelNumberInput(
    modifier: Modifier = Modifier,
    value: Int,
    @StringRes label: Int,
    onChanged: (newVal: Int) -> Unit,
    onNextClicked: () -> Unit
) {
    TextField(
        modifier = modifier,
        value = value.toString(),
        onValueChange = { newVal: String ->
            onChanged(newVal.toInt())
        },
        label = {
            Text(stringResource(label))
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Number
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNextClicked() }
        )
    )
}

@Composable
fun AddModelDropDown(
    @StringRes label: Int,
    value: String,
    items: Array<String>,
    modifier: Modifier = Modifier,
    onChanged: (newVal: String) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    var selectedStatus: String? by remember {
        mutableStateOf("test item")
    }
    val toggleMenu = { isExpanded = !isExpanded }
    val selectMenuItem = { v: String ->
        selectedStatus = v
    }
    Box(
        modifier = modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.TopStart)
    ) {
        Button(onClick = toggleMenu) {
            Text(stringResource(label))
        }
        DropdownMenu(
            expanded = isExpanded,
            onDismissRequest = toggleMenu,
        ) {
            items.map {
                DropdownMenuItem(
                    text = {
                        Text(text=it)
                    },
                    onClick = {
                        selectMenuItem(it)
                        toggleMenu()
                    },
                    trailingIcon = {
                        if (null != selectedStatus && selectedStatus == it) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Выбран"
                            )
                        }
                    }
                )
            }
        }
        Box(modifier = modifier.padding(50.dp, 10.dp).fillMaxSize().wrapContentSize(Alignment.TopEnd)) {
            selectedStatus?.let {
                ModelStatus(selectedStatus)
            }
        }
    }
}


@Composable
fun AddModelSaveButton(
    modifier: Modifier = Modifier
) {
    Button(
        modifier = modifier,
        enabled = true,
        onClick = {}
    ) {
        Text(text = stringResource(R.string.save_new_model_button_label))
    }
}

@Composable
@Preview
fun AddModelFormPreview() {
    val uiState = AddModelUiState()
    AddModelForm(
        uiState = uiState,
        eventHandler = {},
    )
}