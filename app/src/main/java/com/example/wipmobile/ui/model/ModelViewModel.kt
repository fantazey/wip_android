package com.example.wipmobile.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModelViewModel @Inject constructor(
    private val modelsRepository: ModelsRepository
): ViewModel() {
    val uiState = MutableStateFlow(ModelUiState())
    private var modelProgress = emptyList<ModelProgress>()
    private var modelImages = emptyList<ModelImage>()
    private var model: Model? = null

    fun handleEvent(event: ModelEvent) {
        when (event) {
            is ModelEvent.Select -> {
                selectModel(event.model)
            }
            is ModelEvent.ClearError -> {
                clearError()
            }
            is ModelEvent.Refresh -> {
                refresh()
            }
        }
    }

    private fun selectModel(modelToSelect: Model) {
        uiState.value = uiState.value.copy(model=modelToSelect, isLoading = false, loaded = false)
        loadData(modelToSelect)
    }

    private fun loadData(modelToLoad: Model) {
        if (uiState.value.loaded) {
            return
        }
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                model = modelsRepository.loadModel(modelToLoad.id)
                modelImages = modelsRepository.loadModelImages(modelToLoad)
                modelProgress = modelsRepository.loadModelProgress(modelToLoad)
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    loaded = true,
                    model = model,
                    images = modelImages,
                    progress = modelProgress,
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

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }

    private fun refresh() {
        uiState.value = uiState.value.copy(loaded = false)
        loadData(uiState.value.model!!)
    }
}