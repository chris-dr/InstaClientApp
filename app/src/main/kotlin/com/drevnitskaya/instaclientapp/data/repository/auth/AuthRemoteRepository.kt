package com.drevnitskaya.instaclientapp.data.repository.auth

import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.data.remote.api.auth.AuthApiInterface
import com.drevnitskaya.instaclientapp.data.remote.api.auth.entities.TokenResponse
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL

interface AuthRemoteRepository {
    suspend fun getAccessToken(authCode: String): TokenResponse
}

class AuthRemoteRepositoryImpl(
    private val remoteDataSource: AuthApiInterface
) : AuthRemoteRepository {

    override suspend fun getAccessToken(authCode: String): TokenResponse {
        return remoteDataSource.getAccessToken(
            clientId = BuildConfig.INSTA_CLIENT_ID,
            clientSecret = BuildConfig.INSTA_CLIENT_SECRET,
            grantType = "authorization_code",
            redirectUri = AUTH_REDIRECT_URL,
            code = authCode
        )
    }
}