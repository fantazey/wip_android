package com.example.wipmobile.data.source.local

import android.util.Log
import com.example.wipmobile.data.model.AccessToken
import javax.inject.Inject

class AccessTokenWrapper @Inject constructor(private val sharedPreferencesApi: SharedPreferencesApi) {
    private var accessToken: AccessToken? = null

    fun getAccessToken(): AccessToken? {
        if (accessToken == null) {
            try {
                val token = sharedPreferencesApi.getString(SharedPreferencesApi.ACCESS_TOKEN)
                val username = sharedPreferencesApi.getString(SharedPreferencesApi.USERNAME)
                val expirationDate = sharedPreferencesApi.getString(SharedPreferencesApi.ACCESS_TOKEN_EXPIRATION_DATE)
                accessToken = AccessToken(token, username, expirationDate)
            } catch (e: Exception) {
                return null
            }
        }
        return accessToken
    }

    fun setAccessToken(token: AccessToken) {
        sharedPreferencesApi.setString(SharedPreferencesApi.ACCESS_TOKEN, token.token)
        sharedPreferencesApi.setString(SharedPreferencesApi.USERNAME, token.username)
        sharedPreferencesApi.setString(SharedPreferencesApi.ACCESS_TOKEN_EXPIRATION_DATE, token.expirationDate)
        this.accessToken = token
    }
}