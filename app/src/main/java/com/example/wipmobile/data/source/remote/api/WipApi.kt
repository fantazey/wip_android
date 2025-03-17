package com.example.wipmobile.data.source.remote.api

import com.example.wipmobile.data.source.remote.api.request.ModelProgressRequest
import com.example.wipmobile.data.source.remote.api.request.ModelRequest
import com.example.wipmobile.data.source.remote.api.response.BattleScribeCategoryResponse
import com.example.wipmobile.data.source.remote.api.response.BattleScribeUnitResponse
import com.example.wipmobile.data.source.remote.api.response.KillTeamResponse
import com.example.wipmobile.data.source.remote.api.response.ModelGroupResponse
import com.example.wipmobile.data.source.remote.api.response.ModelImageResponse
import com.example.wipmobile.data.source.remote.api.response.ModelProgressResponse
import com.example.wipmobile.data.source.remote.api.response.ModelResponse
import com.example.wipmobile.data.source.remote.api.response.PagedModelResponse
import com.example.wipmobile.data.source.remote.api.response.UserStatusResponse
import okhttp3.MultipartBody
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface WipApi {
    @GET("wip/api/models/")
    suspend fun getUserModels(
        @Query("user_status") statuses: List<Int> = emptyList(),
        @Query("groups") modelGroups: List<Int> = emptyList(),
        @Query("name") name: String = "",
        @Query("page") page: Int = 1
    ): PagedModelResponse

    @GET("wip/api/models/{id}/")
    suspend fun getModel(
        @Path("id") modelId: Int,
    ): ModelResponse

    @GET("wip/api/models/{id}/progress/")
    suspend fun getModelProgress(
        @Path("id") modelId: Int,
    ): List<ModelProgressResponse>

    @GET("wip/api/models/{id}/images/")
    suspend fun getModelImages(
        @Path("id") modelId: Int,
    ): List<ModelImageResponse>

    @GET("wip/api/statuses/")
    suspend fun getUserStatuses(): List<UserStatusResponse>

    @GET("wip/api/kill-teams/")
    suspend fun getKillTeams(): List<KillTeamResponse>

    @GET("wip/api/bs-units/")
    suspend fun getBattleScribeUnits(): List<BattleScribeUnitResponse>

    @GET("wip/api/bs-categories/")
    suspend fun getBattleScribeCategories(): List<BattleScribeCategoryResponse>

    @GET("wip/api/model-groups/")
    suspend fun getModelGroups(): List<ModelGroupResponse>

    @POST("wip/api/models/")
    suspend fun createModel(@Body body: ModelRequest): ModelResponse

    @PUT("wip/api/models/{id}/")
    suspend fun updateModel(@Path("id") modelId: Int, @Body body: ModelRequest): ModelResponse

    @POST("wip/api/models/{id}/progress/")
    suspend fun createModelProgress(@Path("id") modelId: Int, @Body body: ModelProgressRequest): ModelProgressResponse

    @POST("wip/api/models/{id}/progress/")
    suspend fun createModelProgressWithImage(@Path("id") modelId: Int, @Body body: ModelProgressRequest): ModelProgressResponse

    @PUT("wip/api/models/{id}/progress/{progress}/")
    suspend fun updateModelProgress(@Path("id") modelId: Int, @Path("progress") progressId: Int, @Body body: ModelProgressRequest): ModelProgressResponse

    @PUT("wip/api/models/{id}/progress/{progress}/")
    suspend fun updateModelProgressWithImage(@Path("id") modelId: Int, @Path("progress") progressId: Int, @Body body: ModelProgressRequest): ModelProgressResponse

    @DELETE("wip/api/models/{id}/progress/{progress}/")
    suspend fun deleteModelProgress(@Path("id") modelId: Int, @Path("progress") progressId: Int)

    @Multipart
    @POST("wip/api/models/{id}/images/")
    suspend fun createModelImage(@Path("id") modelId: Int, @Part images: Array<MultipartBody.Part>): List<ModelImageResponse>

    @DELETE("wip/api/models/{id}/images/{imageId}/")
    suspend fun deleteModelImage(@Path("id") modelId: Int, @Path("imageId") imageId: Int)
}