package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository

interface ProfileRepository {
    suspend fun getProfile(): DataResponse<Profile>
}

class ProfileRepositoryImpl(
    private val authLocalRepository: AuthLocalRepository,
    private val remoteDataSource: InstaApiInterface
) : ProfileRepository {
    override suspend fun getProfile(): DataResponse<Profile> {
        val token = authLocalRepository.token
        return remoteDataSource.getProfile(token)
    }
}