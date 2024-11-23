package com.example.wipmobile.di

import android.content.Context
import com.example.wipmobile.data.UserRepository
import com.example.wipmobile.data.source.local.AccessTokenWrapper
import com.example.wipmobile.data.source.local.SharedPreferencesApi
import com.example.wipmobile.data.source.remote.UserRemoteDataSource
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun provideSharedPreferencesApi(context: Context): SharedPreferencesApi {
        return SharedPreferencesApi(context)
    }

    @Singleton
    @Provides
    fun provideAccessTokenWrapper(sharedPreferencesApi: SharedPreferencesApi): AccessTokenWrapper {
        return AccessTokenWrapper(sharedPreferencesApi)
    }

    @Singleton
    @Provides
    fun provideUserRepository(remoteDataSource: UserRemoteDataSource, accessTokenWrapper: AccessTokenWrapper): UserRepository {
        return UserRepository(remoteDataSource, accessTokenWrapper)
    }
}