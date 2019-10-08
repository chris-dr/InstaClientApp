package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.DataResponse
import com.drevnitskaya.instaclientapp.data.entities.FeedItem
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository

interface LoadInitialFeedUseCase {
    suspend fun execute(): Result<DataResponse<List<FeedItem>>>
}

class LoadInitialFeedUseCaseImpl(
    private val feedRepository: FeedRepository
) : LoadInitialFeedUseCase {
    override suspend fun execute(): Result<DataResponse<List<FeedItem>>> {
        return try {
            val result = feedRepository.loadInitialFeed()
            Result.Success(result)
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}