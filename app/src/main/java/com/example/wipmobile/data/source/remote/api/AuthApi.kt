package com.example.wipmobile.data.source.remote.api

import com.example.wipmobile.data.model.LoginResponse
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/wip/api/auth/login/")
    suspend fun login(@Header("Authorization") basicAuthHeaderValue: String): LoginResponse

    @POST("/wip/api/auth/logout/")
    suspend fun login()
}