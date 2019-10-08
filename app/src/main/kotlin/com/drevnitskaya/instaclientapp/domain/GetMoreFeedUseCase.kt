package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import java.lang.Exception

interface GetMoreFeedUseCase {
    suspend fun execute(nextUrl: String): UseCaseResult<DataResponse<List<FeedItem>>>
}

class GetMoreFeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : GetMoreFeedUseCase {
    override suspend fun execute(nextUrl: String): UseCaseResult<DataResponse<List<FeedItem>>> {
        return try {
            val result = feedRepository.getRemoteMoreFeed(nextUrl)
            UseCaseResult.Success(result)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}