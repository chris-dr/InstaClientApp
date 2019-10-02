package com.drevnitskaya.instaclientapp.data.remote.api

import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface InstaApiInterface {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("oauth/authorize")
    suspend fun getAuthCode(
        @Field("client_id") clientId: String,
        @Field("redirect_uri") redirectUri: String = AUTH_REDIRECT_URL,
        @Field("response_type") code: String = "code"
    ): String
}