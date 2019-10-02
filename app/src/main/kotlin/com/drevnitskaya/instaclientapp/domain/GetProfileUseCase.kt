package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import java.lang.Exception

interface GetProfileUseCase {
    suspend fun execute(): UseCaseResult<Profile>
}

class GetProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetProfileUseCase {
    override suspend fun execute(): UseCaseResult<Profile> {
        return try {
            val profile = profileRepository.getProfile().data
            UseCaseResult.Success(profile)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}