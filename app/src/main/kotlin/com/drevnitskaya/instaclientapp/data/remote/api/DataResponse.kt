package com.drevnitskaya.instaclientapp.data.remote.api

import com.google.gson.annotations.SerializedName

data class DataResponse<T>(
    var data: T,
    var pagination: Pagination? = null
)

data class Pagination(
    @SerializedName("next_url")
    var nextUrl: String? = null
)