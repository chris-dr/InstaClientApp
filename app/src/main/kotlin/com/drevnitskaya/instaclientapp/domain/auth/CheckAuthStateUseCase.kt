package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CheckAuthStateUseCase {
    suspend fun execute(): Boolean
}

class CheckAuthStateUseCaseImpl(
    private val authLocalRepository: AuthLocalRepository
) : CheckAuthStateUseCase {
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.IO) {
            authLocalRepository.token.isNotEmpty()
        }
    }
}