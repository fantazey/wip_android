package com.example.wipmobile.data.source.remote.api


import com.example.wipmobile.data.model.Model
import retrofit2.http.GET

interface WipApi {
    @GET("wip/api/models/")
    suspend fun getUserModels(): Array<Model>

    @GET("wip/api/statuses/")
    suspend fun getUserStatuses()

}