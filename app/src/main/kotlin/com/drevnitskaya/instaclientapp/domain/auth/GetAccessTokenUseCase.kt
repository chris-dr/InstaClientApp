package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthRemoteRepository
import com.drevnitskaya.instaclientapp.domain.UseCaseResult

interface GetAccessTokenUseCase {
    suspend fun execute(authCode: String): UseCaseResult<String>
}

class GetAccessTokenUseCaseImpl(
    private val authRemoteRepository: AuthRemoteRepository,
    private val authLocalRepository: AuthLocalRepository
) : GetAccessTokenUseCase {
    override suspend fun execute(authCode: String): UseCaseResult<String> {
        return try {
            val token = authRemoteRepository.getAccessToken(authCode).token
            if (token.isNullOrEmpty()) {
                UseCaseResult.Error(Throwable("Invalid access token"))
            } else {
                authLocalRepository.token = token
                UseCaseResult.Complete
            }
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}