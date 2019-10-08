package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.data.source.local.TokenLocalDataSource
import com.drevnitskaya.instaclientapp.data.source.remote.RemoteDataSource
import com.drevnitskaya.instaclientapp.data.entities.TokenResponse
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL

interface AuthRepository {
    suspend fun getAccessToken(authCode: String): TokenResponse

    fun isUserLoggedIn(): Boolean

    fun saveToken(token: String)

    fun clearToken()
}

class AuthRepositoryImpl(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val tokenRemoteDataSource: RemoteDataSource
) : AuthRepository {

    override suspend fun getAccessToken(authCode: String): TokenResponse {
        return tokenRemoteDataSource.getAccessToken(
            clientId = BuildConfig.INSTA_CLIENT_ID,
            clientSecret = BuildConfig.INSTA_CLIENT_SECRET,
            grantType = "authorization_code",
            redirectUri = AUTH_REDIRECT_URL,
            code = authCode
        )
    }

    override fun isUserLoggedIn(): Boolean {
        return tokenLocalDataSource.token.isNotBlank()
    }

    override fun saveToken(token: String) {
        tokenLocalDataSource.token = token
    }

    override fun clearToken() {
        tokenLocalDataSource.token = ""
    }
}