package com.example.wipmobile.data.source.remote.api

import android.util.Log
import com.example.wipmobile.data.source.local.AccessTokenWrapper
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val accessTokenWrapper: AccessTokenWrapper
): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val initialRequest = chain.request()
        Log.i("auth interceptor", "token is: " + accessTokenWrapper.getAccessToken()!!.token)
        val authRequestBuilder = initialRequest.newBuilder().addHeader("Authorization", "Token " + accessTokenWrapper.getAccessToken()!!.token)
        return chain.proceed(authRequestBuilder.build())
    }
}