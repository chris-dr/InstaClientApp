package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.data.local.PreferenceProvider
import com.drevnitskaya.instaclientapp.data.local.SharedPreferenceProvider
import com.drevnitskaya.instaclientapp.data.remote.api.content.InstaContentApiInterface
import com.drevnitskaya.instaclientapp.data.remote.api.interceptors.AccessTokenInterceptor
import com.drevnitskaya.instaclientapp.framework.api.BaseOkHttpClientBuilder
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthRemoteRepository
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthRemoteRepositoryImpl
import com.drevnitskaya.instaclientapp.framework.api.BaseRetrofitClientFactory
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

const val DI_NAME_CONTENT_BASE_URL = "di_content_base_url"
const val DI_HTTP_LOG_INTERCEPTOR = "DI_logging_interceptor"
const val DI_HTTP_TOKEN_INTERCEPTOR = "DI_token_interceptor"
const val DI_HTTP_CONTENT_INTERCEPTORS_LIST = "DI_content_interceptors"
val appModule = module {
    factory {
        BaseOkHttpClientBuilder().init()
    }

    factory<Interceptor>(named(DI_HTTP_LOG_INTERCEPTOR)) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    factory<Interceptor>(named(DI_HTTP_TOKEN_INTERCEPTOR)) {
        AccessTokenInterceptor(
            authLocalRepository = get()
        )
    }

    single<List<Interceptor>>(named(DI_HTTP_CONTENT_INTERCEPTORS_LIST)) {
        mutableListOf<Interceptor>().apply {
            if (BuildConfig.DEBUG) {
                add(get(named(DI_HTTP_LOG_INTERCEPTOR)))
            }
            add(get(named(DI_HTTP_TOKEN_INTERCEPTOR)))
        }
    }

    single<Converter.Factory> { GsonConverterFactory.create() }

    factory<InstaContentApiInterface> {
        BaseRetrofitClientFactory(
            baseOkHttpClientBuilder = get(),
            converterFactory = get(),
            baseUrl = get(named(DI_NAME_CONTENT_BASE_URL))
        ).build()
    }

    factory<AuthRemoteRepository> {
        AuthRemoteRepositoryImpl(
            remoteDataSource = get()
        )
    }

    single<PreferenceProvider> { SharedPreferenceProvider(context = get()) }
}