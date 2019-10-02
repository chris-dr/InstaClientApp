package com.drevnitskaya.instaclientapp.di

import com.drevnitskaya.instaclientapp.framework.api.BaseOkHttpClientBuilder
import com.drevnitskaya.instaclientapp.framework.api.BaseRetrofitClientFactory
import com.drevnitskaya.instaclientapp.data.remote.api.InstaApiInterface
import com.drevnitskaya.instaclientapp.data.repository.UserRepository
import com.drevnitskaya.instaclientapp.data.repository.UserRepositoryImpl
import com.drevnitskaya.instaclientapp.framework.api.INSTA_BASE_URL
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.converter.gson.GsonConverterFactory

const val DI_NAME_BASE_URL = "di_base_url"

val appModule = module {
    single(named(DI_NAME_BASE_URL)) { INSTA_BASE_URL }

    factory {
        BaseOkHttpClientBuilder().init()
    }

    single<Converter.Factory> { GsonConverterFactory.create() }

    factory<InstaApiInterface> {
        BaseRetrofitClientFactory(
            baseOkHttpClientBuilder = get(),
            converterFactory = get(),
            baseUrl = get(named(DI_NAME_BASE_URL))
        ).build()
    }

    factory<UserRepository> {
        UserRepositoryImpl(remoteDataSource = get())
    }
}