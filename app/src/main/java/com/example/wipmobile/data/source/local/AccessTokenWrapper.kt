package com.example.wipmobile.data.source.local

import android.util.Log
import com.example.wipmobile.data.model.AccessToken
import javax.inject.Inject

class AccessTokenWrapper @Inject constructor(private val sharedPreferencesApi: SharedPreferencesApi) {
    private var accessToken: AccessToken? = null

    fun getAccessToken(): AccessToken? {
        if (accessToken == null) {
            Log.i("read token from shared pref api", "")
            accessToken = AccessToken(sharedPreferencesApi.getString(SharedPreferencesApi.ACCESS_TOKEN))
        }
        return accessToken
    }

    fun setAccessToken(token: AccessToken) {
        Log.i("save token:", token.token)
        this.accessToken = token
        Log.i("save token to shared pref api:", token.token)
        sharedPreferencesApi.setString(SharedPreferencesApi.ACCESS_TOKEN, token.token)
    }
}