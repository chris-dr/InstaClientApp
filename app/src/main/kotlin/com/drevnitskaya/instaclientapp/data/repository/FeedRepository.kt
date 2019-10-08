package com.drevnitskaya.instaclientapp.data.repository

import android.util.Log
import com.drevnitskaya.instaclientapp.data.entities.DataResponse
import com.drevnitskaya.instaclientapp.data.source.remote.RemoteDataSource
import com.drevnitskaya.instaclientapp.data.entities.FeedItem
import com.drevnitskaya.instaclientapp.data.source.local.TokenLocalDataSource
import com.drevnitskaya.instaclientapp.data.source.local.FeedLocalDataSource

private const val FEED_PAGE_SIZE = 8

interface FeedRepository {
    suspend fun loadInitialFeed(): DataResponse<List<FeedItem>>

    suspend fun loadMoreFeed(nextUrl: String): DataResponse<List<FeedItem>>
}

class FeedRepositoryImpl(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val feedRemoteDataSource: RemoteDataSource,
    private val feedLocalDataSource: FeedLocalDataSource? = null

) : FeedRepository {
    override suspend fun loadInitialFeed(): DataResponse<List<FeedItem>> {
        val remoteFeed = try {
            feedRemoteDataSource.getInitialFeed(
                token = tokenLocalDataSource.token,
                count = FEED_PAGE_SIZE
            )
        } catch (ex: Exception) {
            null
        }

        if (remoteFeed == null) {
            Log.w(javaClass.canonicalName, "Remote data source fetch failed")
        } else {
            return remoteFeed
        }

        val localFeed = try {
            feedLocalDataSource?.loadFeed()
        } catch (ex: Exception) {
            null
        }

        if (localFeed != null) {
            return localFeed
        } else {
            throw Exception("Error fetching from remote and local")
        }
    }

    override suspend fun loadMoreFeed(nextUrl: String): DataResponse<List<FeedItem>> {
        return feedRemoteDataSource.getMoreFeed(nextUrl)
    }
}