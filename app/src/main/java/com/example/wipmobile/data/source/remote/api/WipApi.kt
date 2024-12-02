package com.example.wipmobile.data.source.remote.api

import com.example.wipmobile.data.source.remote.api.response.BattleScribeUnitResponse
import com.example.wipmobile.data.source.remote.api.response.KillTeamResponse
import com.example.wipmobile.data.source.remote.api.response.PagedModelResponse
import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import retrofit2.http.GET

interface WipApi {
    @GET("wip/api/models/")
    suspend fun getUserModels(): PagedModelResponse

    @GET("wip/api/statuses/")
    suspend fun getUserStatuses(): Array<UserStatusResponse>

    @GET("wip/api/kill-teams/")
    suspend fun getKillTeams(): Array<KillTeamResponse>

    @GET("wip/api/bs-units/")
    suspend fun getBattleScribeUnits(): Array<BattleScribeUnitResponse>
}