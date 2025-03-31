package com.example.wipmobile.data.source.remote.api

import com.example.wipmobile.data.dto.SignUpForm
import com.example.wipmobile.data.source.remote.api.response.LoginResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthApi {
    @POST("/wip/api/auth/login/")
    suspend fun login(@Header("Authorization") basicAuthHeaderValue: String): LoginResponse

    @POST("/wip/api/auth/logout/")
    suspend fun logout(@Header("Authorization") token: String)

    @POST("/wip/api/auth/signup/")
    suspend fun signUp(@Body form: SignUpForm): LoginResponse
}