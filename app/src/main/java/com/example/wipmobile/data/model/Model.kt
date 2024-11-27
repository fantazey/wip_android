package com.example.wipmobile.data.model

data class Model(
    val id: Int,
    val name: String,
    val statusId: Int,
    var statusName: String?,
    val lastImagePath: String?,
    val isTerrain: Boolean,
    val hoursSpent: Double,

    val unitCount: Int,
    val groups: List<ModelGroup>,

    val battleScribeUnitId: Int?,
    val battleScribeUnitName: String?,
    val killTeamId: Int?,
    val killTeamName: String?,
)
