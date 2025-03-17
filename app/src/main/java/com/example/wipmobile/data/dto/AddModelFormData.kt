package com.example.wipmobile.data.dto

import com.example.wipmobile.data.model.BattleScribeUnit
import com.example.wipmobile.data.model.KillTeam
import com.example.wipmobile.data.model.ModelGroup
import com.example.wipmobile.data.model.UserStatus

data class AddModelFormData(
    val name: String = "",
    val unitCount: Int = 1,
    val terrain: Boolean = false,
    val status: UserStatus? = null,
    val groups: List<ModelGroup> = emptyList(),
    val battleScribeUnit: BattleScribeUnit? = null,
    val killTeam: KillTeam? = null
)
