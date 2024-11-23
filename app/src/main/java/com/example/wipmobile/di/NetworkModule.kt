package com.example.wipmobile.di

import com.example.wipmobile.data.source.remote.api.AuthApi
import com.example.wipmobile.data.source.remote.api.AuthInterceptor
import com.example.wipmobile.data.source.remote.api.NetworkClientGenerator
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
        return GsonBuilder().create()
    }


    @Singleton
    @Provides
    fun provideAuthApi(gson: Gson): AuthApi {
        return NetworkClientGenerator.generate("http://10.0.2.2:8000", AuthApi::class.java, gson, emptyArray())
    }

    @Singleton
    @Provides
    fun provideWipApi(gson: Gson, authInterceptor: AuthInterceptor, logInterceptor: HttpLoggingInterceptor): WipApi {
        return NetworkClientGenerator.generate("http://10.0.2.2:8000", WipApi::class.java, gson, arrayOf(authInterceptor, logInterceptor))
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor =  HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

}