package com.example.wipmobile.di

import com.example.wipmobile.data.source.remote.api.AuthApi
import com.example.wipmobile.data.source.remote.api.AuthInterceptor
import com.example.wipmobile.data.source.remote.api.RemoteHost
import com.example.wipmobile.libs.NetworkClientGenerator
import com.example.wipmobile.data.source.remote.api.WipApi
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton

@Module
class NetworkModule {
    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder().setDateFormat("yyyy MM-dd'T'HH:mm:ss.SSSZ").serializeNulls().create()
    }


    @Singleton
    @Provides
    fun provideAuthApi(gson: Gson): AuthApi {
        return NetworkClientGenerator.generate(RemoteHost.HOST, AuthApi::class.java, gson, emptyArray())
    }

    @Singleton
    @Provides
    fun provideWipApi(gson: Gson, authInterceptor: AuthInterceptor, logInterceptor: HttpLoggingInterceptor): WipApi {
        return NetworkClientGenerator.generate(RemoteHost.HOST, WipApi::class.java, gson, arrayOf(authInterceptor, logInterceptor))
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor =  HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}