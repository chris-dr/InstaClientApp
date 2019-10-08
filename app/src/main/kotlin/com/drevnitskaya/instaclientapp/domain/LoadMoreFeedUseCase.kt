package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.FeedWrapper
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import java.lang.Exception

interface LoadMoreFeedUseCase {
    suspend fun execute(nextUrl: String): Result<FeedWrapper>
}

class LoadMoreFeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : LoadMoreFeedUseCase {
    override suspend fun execute(nextUrl: String): Result<FeedWrapper> {
        return try {
            val result = feedRepository.loadMoreFeed(nextUrl)
            Result.Success(result)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}