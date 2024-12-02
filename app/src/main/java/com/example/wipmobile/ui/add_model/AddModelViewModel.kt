package com.example.wipmobile.ui.add_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddModelViewModel @Inject constructor(
    private val modelRepository: ModelsRepository
): ViewModel() {
    val uiState = MutableStateFlow(AddModelUiState())

    fun handleEvent(event: AddModelEvent) {
        when (event) {
            is AddModelEvent.NameChanged -> {
                uiState.update { it.copy(name = event.name) }
            }
            is AddModelEvent.UnitCountChanged -> {
                uiState.update { it.copy(unitCount = event.count) }
            }
            is AddModelEvent.TerrainChanged -> {
                uiState.update { it.copy(terrain = event.isTerrain) }
            }
            is AddModelEvent.KillTeamChanged -> {
                uiState.update { it.copy(killTeamName = event.killTeamName) }
            }
            is AddModelEvent.BattleScribeChanged -> {
                uiState.update { it.copy(killTeamName = event.bsName) }
            }
            is AddModelEvent.GroupsChanged -> {
                uiState.update { it.copy(groupNames = event.groups) }
            }
            is AddModelEvent.StatusChanged -> {
                uiState.update { it.copy(userStatusName = event.statusName) }
            }
            is AddModelEvent.BuyDateChanged -> {
                uiState.update { it.copy(buyDate = event.date) }
            }
            is AddModelEvent.SaveModel -> {
                saveModel(event.successCallback)
            }
            is AddModelEvent.ClearError -> {
                clearError()
            }
        }
    }

    private fun saveModel(successCallback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // todo: save model
                uiState.value = uiState.value.copy(isLoading = false)
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(isLoading = false, error = ex.message)
                }
            }
        }
    }

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }
}