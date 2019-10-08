package com.drevnitskaya.instaclientapp.data.source.remote.api.auth.entities

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    var token: String? = null
)