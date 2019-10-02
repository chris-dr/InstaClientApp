package com.drevnitskaya.instaclientapp.domain.auth

import android.net.Uri
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ParseAuthCodeUseCase {
    suspend fun execute(authUrl: String): UseCaseResult<String>
}

class ParseAuthCodeUseCaseImpl : ParseAuthCodeUseCase {
    override suspend fun execute(authUrl: String): UseCaseResult<String> {
        return withContext(Dispatchers.Default) {
            try {
                val uri = Uri.parse(authUrl)
                val code = uri.getQueryParameter("code")
                if (code.isNullOrEmpty().not()) {
                    UseCaseResult.Success(code)
                } else {
                    UseCaseResult.Error(Throwable("Getting auth code error"))
                }
            } catch (ex: Exception) {
                UseCaseResult.Error(ex)
            }
        }
    }
}