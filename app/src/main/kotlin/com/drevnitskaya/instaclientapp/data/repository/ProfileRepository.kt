package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import com.drevnitskaya.instaclientapp.data.remote.api.content.InstaContentApiInterface

interface ProfileRepository {
    suspend fun getProfile(): DataResponse<Profile>
}

class ProfileRepositoryImpl(
    private val remoteDataSource: InstaContentApiInterface
) : ProfileRepository {
    override suspend fun getProfile(): DataResponse<Profile> {
        return remoteDataSource.getProfile()
    }
}