package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.data.source.local.PreferenceProvider
import com.drevnitskaya.instaclientapp.data.source.local.SharedPreferenceProvider
import com.drevnitskaya.instaclientapp.data.source.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepository
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthLocalRepositoryImpl
import com.drevnitskaya.instaclientapp.framework.api.BaseOkHttpClientBuilder
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthRemoteRepository
import com.drevnitskaya.instaclientapp.data.repository.auth.AuthRemoteRepositoryImpl
import com.drevnitskaya.instaclientapp.framework.api.BaseRetrofitClientFactory
import com.drevnitskaya.instaclientapp.framework.api.INSTA_BASE_URL
import com.drevnitskaya.instaclientapp.utils.NetworkStateProvider
import com.drevnitskaya.instaclientapp.utils.NetworkStateProviderImpl
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

const val DI_NAME_CONTENT_BASE_URL = "di_content_base_url"
const val DI_HTTP_LOG_INTERCEPTOR = "DI_logging_interceptor"
val appModule = module {
    single(named(DI_NAME_CONTENT_BASE_URL)) { INSTA_BASE_URL }
    factory {
        BaseOkHttpClientBuilder().init()
    }

    factory<Interceptor>(named(DI_HTTP_LOG_INTERCEPTOR)) {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single<List<Interceptor>> {
        if (BuildConfig.DEBUG) {
            listOf(get(named(DI_HTTP_LOG_INTERCEPTOR)))
        } else {
            emptyList()
        }
    }

    single<Converter.Factory> { GsonConverterFactory.create() }

    factory<InstaApiInterface> {
        BaseRetrofitClientFactory(
            baseOkHttpClientBuilder = get(),
            converterFactory = get(),
            baseUrl = get(named(DI_NAME_CONTENT_BASE_URL)),
            interceptors = get()
        ).build()
    }

    factory<AuthRemoteRepository> {
        AuthRemoteRepositoryImpl(
            remoteDataSource = get()
        )
    }

    factory<AuthLocalRepository> {
        AuthLocalRepositoryImpl(
            localDataSource = get()
        )
    }

    single<PreferenceProvider> { SharedPreferenceProvider(context = get()) }

    single<NetworkStateProvider> { NetworkStateProviderImpl(context = get()) }
}