package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.source.local.TokenLocalDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CheckAuthStateUseCase {
    suspend fun execute(): Boolean
}

class CheckAuthStateUseCaseImpl(
    private val tokenLocalDataSource: TokenLocalDataSource
) : CheckAuthStateUseCase {
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.IO) {
            tokenLocalDataSource.token.isNotEmpty()
        }
    }
}