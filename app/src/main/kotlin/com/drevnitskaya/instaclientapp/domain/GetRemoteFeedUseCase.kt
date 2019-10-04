package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository

interface GetRemoteFeedUseCase {
    suspend fun execute(): UseCaseResult<List<FeedItem>>
}

class GetRemoteFeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : GetRemoteFeedUseCase {
    override suspend fun execute(): UseCaseResult<List<FeedItem>> {
        return try {
            val result = feedRepository.getRemoteFeed()
            UseCaseResult.Success(result.data)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}