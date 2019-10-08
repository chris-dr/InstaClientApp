package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository
import java.lang.Exception

interface GetProfileUseCase {
    suspend fun execute(): Result<Profile>
}

class GetProfileUseCaseImpl(
    private val profileRepository: ProfileRepository
) : GetProfileUseCase {
    override suspend fun execute(): Result<Profile> {
        return try {
            val profile = profileRepository.getProfile().data
            //TODO: Save it locally
            Result.Success(profile)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}