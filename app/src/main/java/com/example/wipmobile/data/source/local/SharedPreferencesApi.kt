package com.example.wipmobile.data.source.local

import android.content.Context
import javax.inject.Inject


class SharedPreferencesApi @Inject constructor(context: Context){
    private val sharedPreferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    fun setString(key: String, value: String) {
        with(sharedPreferences.edit()) {
            putString(key, value)
            apply()
        }
    }

    fun getString(key: String): String {
        return sharedPreferences.getString(key, "")!!
    }

    companion object {
        private const val PREFERENCES_NAME = "WIP_PREF"
        const val ACCESS_TOKEN = "ACCESS_TOKEN"
        const val USERNAME = "USERNAME"
        const val ACCESS_TOKEN_EXPIRATION_DATE = "ACCESS_TOKEN_EXPIRATION_DATE"
    }
}