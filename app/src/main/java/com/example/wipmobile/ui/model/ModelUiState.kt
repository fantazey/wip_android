package com.example.wipmobile.ui.model

import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress

data class ModelUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val model: Model? = null,
    val loaded: Boolean = false,
    val images: List<ModelImage> = emptyList(),
    val progress: List<ModelProgress> = emptyList()
)