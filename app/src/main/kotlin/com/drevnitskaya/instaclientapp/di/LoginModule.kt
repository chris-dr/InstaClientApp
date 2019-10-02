package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.data.remote.api.auth.AuthApiInterface
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepositoryImpl
import com.drevnitskaya.instaclientapp.domain.auth.*
import com.drevnitskaya.instaclientapp.framework.api.BaseRetrofitClientFactory
import com.drevnitskaya.instaclientapp.framework.api.INSTA_BASE_URL
import com.drevnitskaya.instaclientapp.presentation.login.LoginWebViewModel
import okhttp3.Interceptor
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val DI_NAME_AUTH_BASE_URL = "DI_auth_base_url"
const val DI_HTTP_AUTH_INTERCEPTORS_LIST = "DI_auth_interceptors"

val loginModule = module {
    single(named(DI_NAME_AUTH_BASE_URL)) { INSTA_BASE_URL }

    single<List<Interceptor>>(named(DI_HTTP_AUTH_INTERCEPTORS_LIST)) {
        if (BuildConfig.DEBUG) {
            listOf(get(named(DI_HTTP_LOG_INTERCEPTOR)))
        } else {
            emptyList()
        }
    }

    factory<AuthApiInterface> {
        BaseRetrofitClientFactory(
            baseOkHttpClientBuilder = get(),
            converterFactory = get(),
            baseUrl = get(named(DI_NAME_AUTH_BASE_URL)),
            interceptors = get(named(DI_HTTP_AUTH_INTERCEPTORS_LIST))
        ).build()
    }

    factory<AuthLocalRepository> { AuthLocalRepositoryImpl(localDataSource = get()) }

    factory<ComposeAuthUrlUseCase> { ComposeAuthUrlUseCaseImpl() }

    factory<ParseAuthCodeUseCase> { ParseAuthCodeUseCaseImpl() }

    factory<GetAccessTokenUseCase> {
        GetAccessTokenUseCaseImpl(
            authRemoteRepository = get(),
            authLocalRepository = get()
        )
    }

    viewModel {
        LoginWebViewModel(
            composeAuthUrlUseCase = get(),
            parseAuthCodeUseCase = get(),
            getAccessTokenUseCase = get()
        )
    }
}