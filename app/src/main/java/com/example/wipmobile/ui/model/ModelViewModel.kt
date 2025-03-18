package com.example.wipmobile.ui.model

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.dto.ModelProgressFormData
import com.example.wipmobile.data.model.BattleScribeCategory
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.model.UserStatus
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

    private var userStatuses = emptyList<UserStatus>()
    private var modelGroups = emptyList<ModelGroup>()
    private var killTeams = emptyList<KillTeam>()
    private var battleScribeCategories = emptyList<BattleScribeCategory>()
    private var battleScribeUnits = emptyList<BattleScribeUnit>()

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
            is ModelEvent.UploadImages -> {
                uploadImages(event.model, event.images, event.resetCallback)
            }
            is ModelEvent.UpdateModel -> {
                updateModel(event.model, event.data, event.successCallback)
            }
            is ModelEvent.SelectModelProgress -> {
                selectModelProgress(event.progress)
            }
            is ModelEvent.CreateModelProgress -> {

            }
            is ModelEvent.UpdateModelProgress -> {

            }
            is ModelEvent.DeleteModelProgress -> {

            }
            is ModelEvent.DeleteImage -> {

            }
        }
    }

    private fun selectModel(modelToSelect: Model) {
        uiState.value = uiState.value.copy(model=modelToSelect, isLoading = false, loaded = false, modelProgress = null)
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
                userStatuses = modelsRepository.loadUserStatuses()
                modelGroups = modelsRepository.loadModelGroups()
                killTeams = modelsRepository.loadKillTeams()
                battleScribeCategories = modelsRepository.loadBsCategories()
                battleScribeUnits = modelsRepository.loadBsUnits()

                uiState.value = uiState.value.copy(
                    isLoading = false,
                    loaded = true,
                    model = model,
                    images = modelImages,
                    progress = modelProgress,
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

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }

    private fun refresh() {
        uiState.value = uiState.value.copy(loaded = false)
        loadData(uiState.value.model!!)
    }

    private fun uploadImages(model: Model, images: List<Bitmap>, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.createModelImage(model, images)
                refresh()
                withContext(Dispatchers.Main) {
                    callback()
                }
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

    private fun updateModel(model: Model, formData: ModelFormData, successCallback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.updateModel(model, formData)
                refresh()
                withContext(Dispatchers.Main) {
                    successCallback()
                }
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

    private fun selectModelProgress(modelProgress: ModelProgress) {
        uiState.value = uiState.value.copy(modelProgress = modelProgress)
    }

    private fun createModelProgress(model: Model, formData: ModelProgressFormData, callback: () -> Unit) {

    }

    private fun updateModelProgress(model: Model, modelProgress: ModelProgress, formData: ModelProgressFormData, callback: () -> Unit) {

    }

    private fun deleteModelProgress(model: Model, modelProgress: ModelProgress, callback: () -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.deleteModelProgress(model, modelProgress)
                refresh()
                withContext(Dispatchers.Main) {
                    callback()
                }
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
}