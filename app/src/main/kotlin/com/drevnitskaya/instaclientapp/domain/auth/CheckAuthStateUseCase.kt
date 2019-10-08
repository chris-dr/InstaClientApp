package com.drevnitskaya.instaclientapp.domain.auth

import com.drevnitskaya.instaclientapp.data.repository.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CheckAuthStateUseCase {
    suspend fun execute(): Boolean
}

class CheckAuthStateUseCaseImpl(
    private val authRepository: AuthRepository
) : CheckAuthStateUseCase {
    override suspend fun execute(): Boolean {
        return withContext(Dispatchers.IO) {
            authRepository.isUserLoggedIn()
        }
    }
}