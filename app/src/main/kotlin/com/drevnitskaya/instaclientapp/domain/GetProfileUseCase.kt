package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.ProfileWrapper
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import java.lang.Exception

interface GetProfileUseCase {
    suspend fun execute(): Result<ProfileWrapper>
}

class GetProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetProfileUseCase {
    override suspend fun execute(): Result<ProfileWrapper> {
        return try {
            val profile = profileRepository.getProfile()
            Result.Success(profile)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}