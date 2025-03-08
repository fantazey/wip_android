package com.example.wipmobile.ui.model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.wipmobile.data.ModelsRepository
import com.example.wipmobile.ui.auth.AuthenticationUiState
import com.example.wipmobile.data.UserRepository
import com.example.wipmobile.data.model.Model
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ModelViewModel @Inject constructor(
    private val modelsRepository: ModelsRepository
): ViewModel() {
    val uiState = MutableStateFlow(ModelUiState())

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

    private fun selectModel(model: Model) {
        uiState.value = uiState.value.copy(model=model, isLoading = false)
    }

    private fun clearError() {
        uiState.value = uiState.value.copy(error = null)
    }

    private fun refresh() {

    }
}