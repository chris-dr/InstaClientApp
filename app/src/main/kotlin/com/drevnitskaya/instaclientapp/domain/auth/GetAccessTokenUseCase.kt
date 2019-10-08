package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthRemoteRepository
import com.drevnitskaya.instaclientapp.data.Result

interface GetAccessTokenUseCase {
    suspend fun execute(authCode: String): Result<String>
}

class GetAccessTokenUseCaseImpl(
    private val authRemoteRepository: AuthRemoteRepository,
    private val authLocalRepository: AuthLocalRepository
) : GetAccessTokenUseCase {
    override suspend fun execute(authCode: String): Result<String> {
        return try {
            val token = authRemoteRepository.getAccessToken(authCode).token
            if (token.isNullOrEmpty()) {
                Result.Error(Throwable("Invalid access token"))
            } else {
                authLocalRepository.token = token
                Result.Complete
            }
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}