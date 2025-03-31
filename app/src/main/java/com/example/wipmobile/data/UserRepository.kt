package com.example.wipmobile.data

import com.example.wipmobile.data.dto.SignUpForm
import com.example.wipmobile.data.model.AccessToken
import com.example.wipmobile.data.source.local.AccessTokenWrapper
import com.example.wipmobile.data.source.remote.UserRemoteDataSource
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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

    suspend fun logout() {
        val accessToken = accessTokenWrapper.getAccessToken()
        accessTokenWrapper.clearAccessToken()
        if (null != accessToken && accessToken.token.isNotEmpty()) {
            remoteDataSource.logout(accessToken.token)
        }
    }

    suspend fun signUp(form: SignUpForm): AccessToken {
        return remoteDataSource.signUp(form).also {
            accessTokenWrapper.setAccessToken(it)
        }
    }

    fun isUserLogged(): Boolean {
        val accessToken = accessTokenWrapper.getAccessToken() ?: return false
        try {
            val expiryDate = ZonedDateTime.parse(accessToken.expirationDate, DateTimeFormatter.ISO_OFFSET_DATE_TIME)
            return !expiryDate.isBefore(ZonedDateTime.now())
        } catch (e: Exception) {
            return false
        }
    }
}