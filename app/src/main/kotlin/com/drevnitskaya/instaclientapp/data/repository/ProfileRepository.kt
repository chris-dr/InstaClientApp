package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.data.entities.DataResponse
import com.drevnitskaya.instaclientapp.data.source.remote.RemoteDataSource
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.data.source.local.TokenLocalDataSource

interface ProfileRepository {
    suspend fun getProfile(): DataResponse<Profile>
}

class ProfileRepositoryImpl(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val profileRemoteDataSource: RemoteDataSource
) : ProfileRepository {
    override suspend fun getProfile(): DataResponse<Profile> {
        val token = tokenLocalDataSource.token
        return profileRemoteDataSource.getProfile(token)
    }
}