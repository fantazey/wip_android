package com.example.wipmobile.data.source.remote

import com.example.wipmobile.data.dto.SignUpForm
import com.example.wipmobile.data.model.AccessToken
import com.example.wipmobile.data.source.remote.api.response.LoginResponse
import com.example.wipmobile.data.source.remote.api.AuthApi
import javax.inject.Inject
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi

class UserRemoteDataSource @Inject constructor(private val authApi: AuthApi) {
    @OptIn(ExperimentalEncodingApi::class)
    suspend fun login(username: String, password: String): AccessToken {
        val headerValue = Base64.Default.encode(("$username:$password").encodeToByteArray())
        val response: LoginResponse = authApi.login("Basic $headerValue")
        return AccessToken(response.token, response.user.username, response.expiry)
    }

    suspend fun logout(token: String) {
        authApi.logout("Token $token")
    }

    suspend fun signUp(form: SignUpForm): AccessToken {
        val response: LoginResponse = authApi.signUp(form)
        return AccessToken(response.token, response.user.username, response.expiry)
    }
}