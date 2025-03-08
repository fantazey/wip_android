package com.example.wipmobile.ui.model

import com.example.wipmobile.data.model.Model

data class ModelUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val model: Model? = null,
    val loaded: Boolean = false
)