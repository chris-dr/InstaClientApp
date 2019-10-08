package com.drevnitskaya.instaclientapp.domain.auth

import android.webkit.CookieManager
import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.repository.AuthRepository
import com.drevnitskaya.instaclientapp.data.repository.FeedRepository
import com.drevnitskaya.instaclientapp.data.repository.ProfileRepository

interface LogoutUseCase {
    suspend fun execute(): Result<Nothing>
}

class LogoutUseCaseImpl(
    private val authRepository: AuthRepository,
    private val profileRepository: ProfileRepository,
    private val feedRepository: FeedRepository
) : LogoutUseCase {
    override suspend fun execute(): Result<Nothing> {
        return try {
            authRepository.clearToken()
            CookieManager.getInstance().removeAllCookies(null)
            profileRepository.removeProfile()
            feedRepository.removeFeed()
            Result.Complete
        } catch (ex: Exception) {
            Result.Error(ex)
        }
    }
}