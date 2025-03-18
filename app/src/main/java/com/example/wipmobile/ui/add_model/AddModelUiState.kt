package com.example.wipmobile.ui.add_model

import com.example.wipmobile.data.dto.ModelFormData
import com.example.wipmobile.data.model.BattleScribeCategory
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus

data class AddModelUiState(
    val formData: ModelFormData? = null,
    val loaded: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
    val userStatuses: List<UserStatus> = emptyList(),
    val modelGroups: List<ModelGroup> = emptyList(),
    val killTeams: List<KillTeam> = emptyList(),
    val battleScribeCategories: List<BattleScribeCategory> = emptyList(),
    val battleScribeUnits: List<BattleScribeUnit> = emptyList()
)