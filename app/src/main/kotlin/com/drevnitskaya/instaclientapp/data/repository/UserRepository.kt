package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.data.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.remote.api.TokenResponse
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL

interface UserRepository {
    suspend fun getAccessToken(authCode: String): TokenResponse
}

class UserRepositoryImpl(
    private val remoteDataSource: InstaApiInterface
) : UserRepository {

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