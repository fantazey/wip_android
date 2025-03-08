package com.example.wipmobile.ui.models

import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus

data class ModelsUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val models: List<Model> = emptyList(),
    val loaded: Boolean = false,
    // filtering
    val availableStatuses: List<UserStatus> = emptyList(),
    val selectedStatuses: List<UserStatus> = emptyList(),
    val availableGroups: List<ModelGroup> = emptyList(),
    val selectedGroups: List<ModelGroup> = emptyList(),
    val nameQuery: String = "",
    val count: Int = 0,
    // paging
    val pagesCount: Int = 0,
    val currentPage: Int = 1,
    val pageSize: Int = 1,
)