package com.example.wipmobile.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Newspaper
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.example.wipmobile.WipScreen

@Composable
fun BottomNavigationBar(currentScreen: WipScreen, onClick: (screen: String) -> Unit) {
    val orderedScreens = arrayOf(
        WipScreen.Models,
        WipScreen.AddProgress,
        WipScreen.Works,
        WipScreen.AddModel,
        WipScreen.Profile
    )
    val orderedScreenIcons = arrayOf(
        Icons.Default.List,
        Icons.Default.Timer,
        Icons.Default.Newspaper,
        Icons.Default.Add,
        Icons.Default.Person
    )
    NavigationBar() {
        orderedScreens.mapIndexed { index, wipScreen ->
            NavigationBarItem(
                selected = wipScreen == currentScreen,
                onClick = { onClick(wipScreen.name) },
                icon = {
                    Icon(
                        imageVector = orderedScreenIcons[index],
                        contentDescription = null,
                    )
                },
                label = {
                    BottomNavBarTextLabel(wipScreen)
                }
            )
        }
    }
}

@Composable
fun BottomNavBarTextLabel(screen: WipScreen) {
    Text(
        text = stringResource(screen.title),
        textAlign = TextAlign.Center
    )
}