package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.UserRepository
import com.drevnitskaya.instaclientapp.domain.UseCaseResult

interface GetAccessTokenUseCase {
    suspend fun execute(authCode: String): UseCaseResult<String>
}

class GetAccessTokenUseCaseImpl(
    private val userRepository: UserRepository
) : GetAccessTokenUseCase {
    override suspend fun execute(authCode: String): UseCaseResult<String> {
        return try {
            val token = userRepository.getAccessToken(authCode).token
            if (token.isNullOrEmpty()) {
                UseCaseResult.Error(Throwable("Invalid access token"))
            } else {
                UseCaseResult.Success(token)
            }
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}