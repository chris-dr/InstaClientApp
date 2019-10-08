package com.drevnitskaya.instaclientapp.data.repository

import android.util.Log
import com.drevnitskaya.instaclientapp.data.source.remote.RemoteDataSource
import com.drevnitskaya.instaclientapp.data.entities.FeedWrapper
import com.drevnitskaya.instaclientapp.data.source.local.TokenLocalDataSource
import com.drevnitskaya.instaclientapp.data.source.local.dao.FeedLocalDataSource

private const val FEED_PAGE_SIZE = 8

interface FeedRepository {
    suspend fun loadInitialFeed(): FeedWrapper

    suspend fun loadMoreFeed(nextUrl: String): FeedWrapper
}

class FeedRepositoryImpl(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val feedRemoteDataSource: RemoteDataSource,
    private val feedLocalDataSource: FeedLocalDataSource
) : FeedRepository {
    override suspend fun loadInitialFeed(): FeedWrapper {
        val remoteFeedResponse = try {
            feedRemoteDataSource.getInitialFeed(
                token = tokenLocalDataSource.token,
                count = FEED_PAGE_SIZE
            )
        } catch (ex: Exception) {
            null
        }

        if (remoteFeedResponse == null) {
            Log.w(javaClass.canonicalName, "Remote data source fetch failed")
        } else {
            val remoteFeed = remoteFeedResponse.data
            feedLocalDataSource.saveFeed(remoteFeed)
            return FeedWrapper(feed = remoteFeed, nextUrl = remoteFeedResponse.pagination?.nextUrl)
        }

        val localFeed = try {
            feedLocalDataSource.getFeed()
        } catch (ex: Exception) {
            null
        }

        if (localFeed != null) {
            return FeedWrapper(feed = localFeed, fromCache = true)
        } else {
            throw Exception("Error fetching from remote and local")
        }
    }

    override suspend fun loadMoreFeed(nextUrl: String): FeedWrapper {
        val response = feedRemoteDataSource.getMoreFeed(nextUrl)
        return FeedWrapper(feed = response.data, nextUrl = response.pagination?.nextUrl)
    }
}