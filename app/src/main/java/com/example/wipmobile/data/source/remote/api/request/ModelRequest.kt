package com.example.wipmobile.data.source.remote.api.request

import com.google.gson.annotations.SerializedName

data class ModelRequest(
    val name: String = "",
    @SerializedName("unit_count")
    val unitCount: Int = 1,
    val terrain: Boolean = false,
    @SerializedName("user_status_id")
    val status: Int,
    @SerializedName("groups_id")
    val groups: List<Int>,
    @SerializedName("battlescribe_unit_id")
    val battleScribeUnit: Int?,
    @SerializedName("kill_team_id")
    val killTeam: Int?
)