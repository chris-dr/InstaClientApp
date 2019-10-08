package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.DataResponse
import com.drevnitskaya.instaclientapp.data.entities.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import java.lang.Exception

interface GetMoreFeedUseCase {
    suspend fun execute(nextUrl: String): Result<DataResponse<List<FeedItem>>>
}

class GetMoreFeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : GetMoreFeedUseCase {
    override suspend fun execute(nextUrl: String): Result<DataResponse<List<FeedItem>>> {
        return try {
            val result = feedRepository.loadMoreFeed(nextUrl)
            Result.Success(result)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}