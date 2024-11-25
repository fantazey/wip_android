package com.example.wipmobile.ui.models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.data.ModelsUiState
import com.example.wipmobile.data.model.UserStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModelsViewModel @Inject constructor(
    private val modelsRepository: ModelsRepository
): ViewModel() {
    val uiState = MutableStateFlow(ModelsUiState())
    private var userStatusesLoaded = false
    private var userStatuses = emptyArray<UserStatus>()

    fun handleEvent(event: ModelsEvent) {
        when (event) {
            is ModelsEvent.ModelsLoad -> {
                loadModels()
            }
            is ModelsEvent.ModelsLoaded -> {
                modelsLoaded()
            }
            is ModelsEvent.ModelsSelectPage -> {
                selectPage(event.page)
            }
        }
    }

    private fun loadModels() {
        if (uiState.value.modelsLoaded) {
            uiState.value = uiState.value.copy(isLoading = false)
            return
        }

        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                if (!userStatusesLoaded) {
                    userStatuses = modelsRepository.loadUserStatuses()
                    userStatusesLoaded = true
                }
                val modelsArray = modelsRepository.loadModels()
                modelsArray.forEach { model ->
                    model.statusName = userStatuses.find { it.id == model.statusId }?.name
                }
                uiState.value = uiState.value.copy(modelResponses = modelsArray, isLoading = false, modelsLoaded = true)
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState.value = uiState.value.copy(error = e.message, isLoading = false, modelsLoaded = true)
                }
            }
        }
    }

    private fun selectPage(page: Int) {
        uiState.value = uiState.value.copy(error = "Еще не реализовано ")
    }

    private fun modelsLoaded() {
        uiState.value = uiState.value.copy(error = "Ошибка")
    }


}