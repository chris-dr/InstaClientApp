package com.drevnitskaya.instaclientapp.domain.auth

import android.net.Uri
import com.drevnitskaya.instaclientapp.data.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ParseAuthCodeUseCase {
    suspend fun execute(authUrl: String): Result<String>
}

class ParseAuthCodeUseCaseImpl : ParseAuthCodeUseCase {
    override suspend fun execute(authUrl: String): Result<String> {
        return withContext(Dispatchers.Default) {
            try {
                val uri = Uri.parse(authUrl)
                val code = uri.getQueryParameter("code")
                if (code.isNullOrEmpty().not()) {
                    Result.Success(code)
                } else {
                    Result.Error(Throwable("Getting auth code error"))
                }
            } catch (ex: Exception) {
                Result.Error(ex)
            }
        }
    }
}