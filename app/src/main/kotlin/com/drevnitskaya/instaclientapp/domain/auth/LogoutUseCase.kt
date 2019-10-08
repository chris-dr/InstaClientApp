package com.drevnitskaya.instaclientapp.domain.auth

import android.webkit.CookieManager
import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.repository.AuthRepository

interface LogoutUseCase {
    suspend fun execute(): Result<Nothing>
}

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository
) : LogoutUseCase {
    override suspend fun execute(): Result<Nothing> {
        return try {
            authRepository.clearToken()
            CookieManager.getInstance().removeAllCookies(null)
            Result.Complete
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}