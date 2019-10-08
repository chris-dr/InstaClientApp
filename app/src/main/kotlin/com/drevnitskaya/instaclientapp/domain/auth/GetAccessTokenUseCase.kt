package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.AuthRepository
import com.drevnitskaya.instaclientapp.data.Result

interface GetAccessTokenUseCase {
    suspend fun execute(authCode: String): Result<String>
}

class GetAccessTokenUseCaseImpl(
    private val authRepository: AuthRepository
) : GetAccessTokenUseCase {
    override suspend fun execute(authCode: String): Result<String> {
        return try {
            val token = authRepository.getAccessToken(authCode).token
            if (token.isNullOrEmpty()) {
                Result.Error(Throwable("Invalid access token"))
            } else {
                authRepository.saveToken(token)
                Result.Complete
            }
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}