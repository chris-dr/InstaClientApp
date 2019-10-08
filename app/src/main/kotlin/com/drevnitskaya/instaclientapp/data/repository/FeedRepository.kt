package com.drevnitskaya.instaclientapp.data.repository

import com.drevnitskaya.instaclientapp.data.source.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.source.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.source.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository

private const val FEED_PAGE_SIZE = 8

interface FeedRepository {
    suspend fun getRemoteInitialFeed(): DataResponse<List<FeedItem>>

    suspend fun getRemoteMoreFeed(nextUrl: String): DataResponse<List<FeedItem>>
}

class FeedRepositoryImpl(
    private val authLocalRepository: AuthLocalRepository,
    private val remoteDataSource: InstaApiInterface
) : FeedRepository {
    override suspend fun getRemoteInitialFeed(): DataResponse<List<FeedItem>> {
        val token = authLocalRepository.token
        return remoteDataSource.getInitialFeed(token = token, count = FEED_PAGE_SIZE)
    }

    override suspend fun getRemoteMoreFeed(nextUrl: String): DataResponse<List<FeedItem>> {
        return remoteDataSource.getMoreFeed(nextUrl)
    }
}