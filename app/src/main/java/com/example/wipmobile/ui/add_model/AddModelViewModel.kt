package com.example.wipmobile.ui.add_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.data.dto.AddModelFormData
import com.example.wipmobile.data.model.BattleScribeCategory
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddModelViewModel @Inject constructor(
    private val modelsRepository: ModelsRepository
) : ViewModel() {
    val uiState = MutableStateFlow(AddModelUiState())

    private var userStatuses = emptyList<UserStatus>()
    private var modelGroups = emptyList<ModelGroup>()
    private var killTeams = emptyList<KillTeam>()
    private var battleScribeCategories = emptyList<BattleScribeCategory>()
    private var battleScribeUnits = emptyList<BattleScribeUnit>()

    fun handleEvent(event: AddModelEvent) {
        when (event) {
            is AddModelEvent.SaveModel -> {
                saveModel(event.formData, event.successCallback, event.errorCallback)
            }

            is AddModelEvent.ClearError -> {
                clearError()
            }

            is AddModelEvent.LoadData -> {
                loadData()
            }
        }
    }

    private fun loadData() {
        if (uiState.value.loaded) {
            return
        }
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userStatuses = modelsRepository.loadUserStatuses()
                modelGroups = modelsRepository.loadModelGroups()
                killTeams = modelsRepository.loadKillTeams()
                battleScribeCategories = modelsRepository.loadBsCategories()
                battleScribeUnits = modelsRepository.loadBsUnits()

                uiState.value = uiState.value.copy(
                    isLoading = false,
                    loaded = true,
                    userStatuses = userStatuses,
                    modelGroups = modelGroups,
                    killTeams = killTeams,
                    battleScribeUnits = battleScribeUnits,
                    battleScribeCategories = battleScribeCategories
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        error = e.message,
                        isLoading = false,
                        loaded = true
                    )
                }
            }
        }
    }

    private fun saveModel(
        formData: AddModelFormData,
        successCallback: () -> Unit,
        errorCallback: () -> Unit
    ) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.createModel(formData)
                uiState.value = uiState.value.copy(isLoading = false)
                successCallback()
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(isLoading = false, error = ex.message)
                    errorCallback()
                }
            }
        }
    }

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }
}