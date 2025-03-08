package com.example.wipmobile.data.source.remote.api

import com.example.wipmobile.data.source.remote.api.response.BattleScribeUnitResponse
import com.example.wipmobile.data.source.remote.api.response.KillTeamResponse
import com.example.wipmobile.data.source.remote.api.response.ModelGroupResponse
import com.example.wipmobile.data.source.remote.api.response.PagedModelResponse
import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WipApi {
    @GET("wip/api/models/")
    suspend fun getUserModels(
        @Query("user_status") statuses: List<Int> = emptyList(),
        @Query("groups") modelGroups: List<Int> = emptyList(),
        @Query("name") name: String = "",
        @Query("page") page: Int = 1
    ): PagedModelResponse

    @GET("wip/api/statuses/")
    suspend fun getUserStatuses(): List<UserStatusResponse>

    @GET("wip/api/kill-teams/")
    suspend fun getKillTeams(): List<KillTeamResponse>

    @GET("wip/api/bs-units/")
    suspend fun getBattleScribeUnits(): List<BattleScribeUnitResponse>

    @GET("wip/api/model-groups/")
    suspend fun getModelGroups(): List<ModelGroupResponse>
}