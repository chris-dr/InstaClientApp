package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.remote.api.InstaMedia
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository

interface InstaMediaRepository {
    suspend fun getRemoteMedia(): DataResponse<List<InstaMedia>>
}

class InstaMediaRepositoryImpl(
    private val authLocalRepository: AuthLocalRepository,
    private val remoteDataSource: InstaApiInterface
) : InstaMediaRepository {
    override suspend fun getRemoteMedia(): DataResponse<List<InstaMedia>> {
        val token = authLocalRepository.token
        return remoteDataSource.getMedia(token = token, maxId = 0, minId = 0, count = 10)
    }
}