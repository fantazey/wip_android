package com.example.wipmobile.ui.profile

import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus

data class ProfileUiState(
    val isLoading: Boolean = false,
    val loaded: Boolean = false,
    val error: String? = null,
    val statuses: List<UserStatus> = emptyList(),
    val groups: List<ModelGroup> = emptyList()
)
