package com.example.wipmobile.data.model

data class Model(
    val id: Int,
    val name: String,
    val status: UserStatus,
    val lastImagePath: String?,
    val isTerrain: Boolean,
    val hoursSpent: Double,
    val unitCount: Int,
    val groups: List<ModelGroup>,

    val battleScribeUnit: BattleScribeUnit?,
    val killTeam: KillTeam?
)
