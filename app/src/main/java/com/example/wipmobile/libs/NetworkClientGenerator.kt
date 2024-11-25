package com.example.wipmobile.libs

import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkClientGenerator {
    companion object {
        fun <T>  generate(baseUrl: String, serviceClass: Class<T>, gson: Gson, interceptors: Array<Interceptor>): T {
            val okHttpClientBuilder = OkHttpClient().newBuilder()
            interceptors.forEach {
                okHttpClientBuilder.addInterceptor(it)
            }
            okHttpClientBuilder.connectTimeout(30L, TimeUnit.SECONDS)
            okHttpClientBuilder.readTimeout(30L, TimeUnit.SECONDS)
            val okHttpClient = okHttpClientBuilder.build()
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build()
            return retrofit.create(serviceClass)
        }
    }
}