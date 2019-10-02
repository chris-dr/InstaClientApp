package com.drevnitskaya.instaclientapp.framework.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit

class BaseRetrofitClientFactory(
    val baseOkHttpClientBuilder: OkHttpClient.Builder,
    val converterFactory: Converter.Factory,
    var baseUrl: String,
    var interceptors: List<Interceptor>? = null
) {

    inline fun <reified T> build(): T {
        return Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .client(baseOkHttpClientBuilder.apply {
                interceptors?.forEach {
                    addInterceptor(it)
                }
            }.build())
            .baseUrl(baseUrl)
            .build()
            .create(T::class.java)
    }
}