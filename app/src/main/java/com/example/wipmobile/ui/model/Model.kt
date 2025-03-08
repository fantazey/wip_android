package com.example.wipmobile.ui.model

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.ui.models.ModelData

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelCard(
    model: Model,
    isLoading: Boolean,
    handleEvent: (e: ModelEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = arrayOf("Модель", "Лог работ", "Галерея")
    PullToRefreshBox(
        isRefreshing = isLoading,
        onRefresh = {
            handleEvent(ModelEvent.Refresh)
        }
    ) {
        Column(modifier = modifier.fillMaxWidth()) {
            PrimaryTabRow(
                selectedTabIndex = selectedTab
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
                    ModelWorkLog()
                }

                2 -> {
                    ModelPictures()
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
        name = "test",
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
    ModelCard(model = model, isLoading = false, handleEvent = {})
}


@Composable
fun ModelMain(model: Model) {
    Column(modifier = Modifier.fillMaxWidth()) {
        ModelData(model, modifier = Modifier
            .fillMaxWidth()
            .padding(PaddingValues(5.dp, 0.dp)))
    }
}


@Composable
fun ModelWorkLog() {
    Text("Лог работ")
}

@Composable
fun ModelPictures() {
    Text("Галерея")
}