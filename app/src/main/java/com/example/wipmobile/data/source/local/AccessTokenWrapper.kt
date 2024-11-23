package com.example.wipmobile.data.source.local

import com.example.wipmobile.data.model.AccessToken
import javax.inject.Inject

class AccessTokenWrapper @Inject constructor(private val sharedPreferencesApi: SharedPreferencesApi) {
    private var accessToken: AccessToken? = null

    fun getAccessToken(): AccessToken? {
        if (accessToken == null) {
            accessToken = AccessToken(sharedPreferencesApi.getString(SharedPreferencesApi.ACCESS_TOKEN))
        }
        return accessToken
    }

    fun setAccessToken(token: AccessToken) {
        this.accessToken = token
        sharedPreferencesApi.setString(SharedPreferencesApi.ACCESS_TOKEN, token.token)
    }
}