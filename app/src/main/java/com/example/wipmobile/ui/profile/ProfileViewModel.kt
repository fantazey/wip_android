package com.example.wipmobile.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.ui.auth.AuthenticationUiState
import com.example.wipmobile.data.UserRepository
import com.example.wipmobile.data.dto.ModelGroupFormData
import com.example.wipmobile.data.dto.StatusFormData
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val modelsRepository: ModelsRepository
): ViewModel() {
    val uiState = MutableStateFlow(ProfileUiState())
    private var userStatuses = emptyList<UserStatus>()
    private var modelGroups = emptyList<ModelGroup>()

    fun handleEvent(event: ProfileEvent) {
        when (event) {
            is ProfileEvent.LoadData -> {
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
            }
            is ProfileEvent.ClearError -> {
                clearError()
            }
            is ProfileEvent.CreateStatus -> {
                createStatus(event.form, event.successCallback)
            }
            is ProfileEvent.UpdateStatus -> {
                updateStatus(event.status, event.form, event.successCallback)
            }
            is ProfileEvent.DeleteStatus -> {
                deleteStatus(event.status, event.successCallback)
            }
            is ProfileEvent.CreateModelGroup -> {
                createModelGroup(event.form, event.successCallback)
            }
            is ProfileEvent.UpdateModelGroup -> {
                updateModelGroup(event.group, event.form, event.successCallback)
            }
            is ProfileEvent.DeleteModelGroup -> {
                deleteModelGroup(event.group, event.successCallback)
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
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    loaded = true,
                    statuses = userStatuses,
                    groups = modelGroups,
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

    private fun createStatus(form: StatusFormData, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.createStatus(form)
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
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

    private fun updateStatus(status: UserStatus, form: StatusFormData, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.updateStatus(status, form)
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
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

    private fun deleteStatus(status: UserStatus, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.deleteStatus(status)
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
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

    private fun createModelGroup(form: ModelGroupFormData, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.createModelGroup(form)
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
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

    private fun updateModelGroup(group: ModelGroup, form: ModelGroupFormData, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.updateModelGroup(group, form)
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
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

    private fun deleteModelGroup(group: ModelGroup, callback: () -> Unit) {
        uiState.value = uiState.value.copy(isLoading = true)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                modelsRepository.deleteModelGroup(group)
                uiState.value = uiState.value.copy(loaded = false)
                loadData()
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