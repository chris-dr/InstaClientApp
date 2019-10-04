package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository

interface FeedRepository {
    suspend fun getRemoteFeed(): DataResponse<List<FeedItem>>
}

class FeedRepositoryImpl(
    private val authLocalRepository: AuthLocalRepository,
    private val remoteDataSource: InstaApiInterface
) : FeedRepository {
    override suspend fun getRemoteFeed(): DataResponse<List<FeedItem>> {
        val token = authLocalRepository.token
        return remoteDataSource.getFeed(token = token, maxId = 0, minId = 0, count = 10)
    }
}