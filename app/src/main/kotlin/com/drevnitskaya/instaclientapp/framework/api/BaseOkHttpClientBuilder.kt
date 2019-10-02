package com.drevnitskaya.instaclientapp.framework.api

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class BaseOkHttpClientBuilder {

    fun init(): OkHttpClient.Builder {
        return OkHttpClient.Builder()
            .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
            .connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
    }
}