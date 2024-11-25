package com.example.wipmobile.data.source.remote.api


import com.example.wipmobile.data.source.remote.api.response.PagedModelResponse
import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import retrofit2.http.GET

interface WipApi {
    @GET("wip/api/models/")
    suspend fun getUserModels(): PagedModelResponse

    @GET("wip/api/statuses/")
    suspend fun getUserStatuses(): Array<UserStatusResponse>

}