package com.drevnitskaya.instaclientapp.domain

import com.drevnitskaya.instaclientapp.data.remote.api.InstaMedia
import com.drevnitskaya.instaclientapp.data.repository.InstaMediaRepository

interface GetRemoteMediaUseCase {
    suspend fun execute(): UseCaseResult<List<InstaMedia>>
}

class GetRemoteMediaUseCaseImpl(
    private val mediaRepository: InstaMediaRepository
) : GetRemoteMediaUseCase {
    override suspend fun execute(): UseCaseResult<List<InstaMedia>> {
        return try {
            val result = mediaRepository.getRemoteMedia()
            UseCaseResult.Success(result.data)
        } catch (ex: Exception) {
            UseCaseResult.Error(ex)
        }
    }
}