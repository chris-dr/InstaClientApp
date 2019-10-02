package com.drevnitskaya.instaclientapp.data.remote.api.interceptors

import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository
import okhttp3.Interceptor
import okhttp3.Response

private const val HEADER_AUTH_KEY = "Authorization"
private const val HEADER_AUTH_VALUE = "Bearer"

class AccessTokenInterceptor(
    private val authLocalRepository: AuthLocalRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        //TODO: Handle how to get this token!!!!
        val token = authLocalRepository.token
        val request =
            chain.request().newBuilder().addHeader(HEADER_AUTH_KEY, "$HEADER_AUTH_VALUE $token")
                .build()
        return chain.proceed(request)
    }

}