package com.example.wipmobile.data.source.remote.api.request

import com.example.wipmobile.data.source.remote.api.response.BattleScribeUnitResponse
import com.example.wipmobile.data.source.remote.api.response.KillTeamResponse
import com.example.wipmobile.data.source.remote.api.response.ModelGroupResponse
import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import com.google.gson.annotations.SerializedName

/**
 * "name": "Necron Psychomancer 1",
 *   "unit_count": 44,
 *   "terrain": true,
 *   "get_last_image_url": "/media/wip/andy/Necron%20Psychomancer/fantazey_AgACAgIAAxkBAAIWJmchUBXHLKRsKDP2EzUvdduY6ImmAAJE4TEb7w_ctVQMbJ",
 *   "user_status": {
 *     "id": 2,
 *     "name": "Лежит в шкафу",
 *     "order": 2,
 *     "previous": 1,
 *     "next": 3
 *   },
 *   "groups": [],
 *   "battlescribe_unit": null,
 *   "kill_team": null,
 *   "hours_spent": 7.223333333333334
 */
data class ModelRequest(
    val name: String = "",
    @SerializedName("unit_count")
    val unitCount: Int = 1,
    val terrain: Boolean = false,
    @SerializedName("user_status")
    val status: UserStatusResponse,
    val groups: List<ModelGroupResponse>,
    @SerializedName("battlescribe_unit")
    val battleScribeUnit: BattleScribeUnitResponse?,
    @SerializedName("kill_team")
    val killTeam: KillTeamResponse?
)