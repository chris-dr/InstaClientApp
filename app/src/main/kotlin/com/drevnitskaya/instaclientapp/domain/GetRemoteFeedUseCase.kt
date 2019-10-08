package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository

interface GetRemoteFeedUseCase {
    suspend fun execute(): UseCaseResult<DataResponse<List<FeedItem>>>
}

class GetRemoteFeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : GetRemoteFeedUseCase {
    override suspend fun execute(): UseCaseResult<DataResponse<List<FeedItem>>> {
        return try {
            val result = feedRepository.getRemoteInitialFeed()
            UseCaseResult.Success(result)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}