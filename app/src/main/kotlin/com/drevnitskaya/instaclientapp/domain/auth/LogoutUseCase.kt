package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import com.drevnitskaya.instaclientapp.domain.UseCaseResult

interface LogoutUseCase {
    suspend fun execute(): UseCaseResult<Nothing>
}

class LogoutUseCaseImpl(
    private val profileRepository: ProfileRepository
) : LogoutUseCase {
    override suspend fun execute(): UseCaseResult<Nothing> {
        return try {
            profileRepository.logout()
            UseCaseResult.Complete
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}