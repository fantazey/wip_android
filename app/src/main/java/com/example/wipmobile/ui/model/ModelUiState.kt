package com.example.wipmobile.ui.model

import com.example.wipmobile.data.model.BattleScribeCategory
import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.Model
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.ModelImage
import com.example.wipmobile.data.model.ModelProgress
import com.example.wipmobile.data.model.UserStatus

data class ModelUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val model: Model? = null,
    val modelProgress: ModelProgress? = null,
    val loaded: Boolean = false,
    val images: List<ModelImage> = emptyList(),
    val progress: List<ModelProgress> = emptyList(),
    // formdata
    val userStatuses: List<UserStatus> = emptyList(),
    val modelGroups: List<ModelGroup> = emptyList(),
    val killTeams: List<KillTeam> = emptyList(),
    val battleScribeCategories: List<BattleScribeCategory> = emptyList(),
    val battleScribeUnits: List<BattleScribeUnit> = emptyList()
)