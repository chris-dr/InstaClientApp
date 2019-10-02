package com.drevnitskaya.instaclientapp.data.remote.api

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    var token: String = ""
)