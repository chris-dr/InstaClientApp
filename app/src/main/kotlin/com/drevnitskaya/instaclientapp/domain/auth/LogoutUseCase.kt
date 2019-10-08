package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import com.drevnitskaya.instaclientapp.data.Result

interface LogoutUseCase {
    suspend fun execute(): Result<Nothing>
}

class LogoutUseCaseImpl(
    private val profileRepository: ProfileRepository
) : LogoutUseCase {
    override suspend fun execute(): Result<Nothing> {
        return try {
            profileRepository.logout()
            Result.Complete
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}