package com.example.wipmobile.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModelsViewModel @Inject constructor(
    private val modelsRepository: ModelsRepository
) : ViewModel() {
    val uiState = MutableStateFlow(ModelsUiState())
    private var userStatuses = emptyList<UserStatus>()
    private var models = emptyList<Model>()
    private var modelGroups = emptyList<ModelGroup>()

    fun handleEvent(event: ModelsEvent) {
        when (event) {
            is ModelsEvent.Load -> {
                loadModels()
            }

            is ModelsEvent.SelectPage -> {
                selectPage(event.page)
            }

            is ModelsEvent.ClearError -> {
                clearError()
            }

            is ModelsEvent.RefreshList -> {
                refreshList()
            }

            is ModelsEvent.ChangeUserStatusFilter -> {
                changeUserStatusFilter(event.values)
            }

            is ModelsEvent.ChangeSearchQuery -> {
                changeSearchQuery(event.value)
            }

            is ModelsEvent.ChangeModelGroupFilter -> {
                changeModelGroupFilter(event.values)
            }

            is ModelsEvent.ApplyFilter -> {
                refreshList()
            }
        }
    }

    private fun loadModels() {
        if (uiState.value.loaded) {
            return
        }

        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                userStatuses = modelsRepository.loadUserStatuses()
                modelGroups = modelsRepository.loadModelGroups()
                models = modelsRepository.loadModels(
                    modelGroups = uiState.value.selectedGroups,
                    statuses = uiState.value.selectedStatuses,
                    name = uiState.value.nameQuery
                )
                uiState.value = uiState.value.copy(
                    models = models,
                    isLoading = false,
                    loaded = true,
                    availableStatuses = userStatuses,
                    availableGroups = modelGroups
                )
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(
                        error = e.message,
                        isLoading = false,
                        models = emptyList(),
                        loaded = true
                    )
                }
            }
        }
    }

    private fun selectPage(page: Int) {
        uiState.value = uiState.value.copy(error = "Еще не реализовано ")
    }

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }

    private fun refreshList() {
        uiState.value = uiState.value.copy(isLoading = true, loaded = false)
        loadModels()
    }

    private fun changeModelGroupFilter(values: List<ModelGroup>) {
        uiState.value = uiState.value.copy(selectedGroups = values)
    }

    private fun changeUserStatusFilter(values: List<UserStatus>) {
        uiState.value = uiState.value.copy(selectedStatuses = values)
    }

    private fun changeSearchQuery(value: String) {
        uiState.value = uiState.value.copy(nameQuery = value)
    }
}