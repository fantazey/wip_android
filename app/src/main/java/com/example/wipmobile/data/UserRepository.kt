package com.example.wipmobile.data

import com.example.wipmobile.data.model.AccessToken
import com.example.wipmobile.data.source.local.AccessTokenWrapper
import com.example.wipmobile.data.source.remote.UserRemoteDataSource
import javax.inject.Inject

class UserRepository @Inject constructor(
    private val remoteDataSource: UserRemoteDataSource,
    private val accessTokenWrapper: AccessTokenWrapper
) {

    suspend fun login(username: String, password: String): AccessToken {
        return remoteDataSource.login(username, password).also {
            accessTokenWrapper.setAccessToken(it)
        }
    }

    fun userLogged(): Boolean {
        return accessTokenWrapper.getAccessToken() != null
    }
}