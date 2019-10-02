package com.drevnitskaya.instaclientapp.data.remote.api.auth

import com.drevnitskaya.instaclientapp.data.remote.api.auth.entities.TokenResponse
import retrofit2.http.*

interface AuthApiInterface {

    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("oauth/access_token")
    suspend fun getAccessToken(
        @Field("client_id") clientId: String,
        @Field("client_secret") clientSecret: String,
        @Field("grant_type") grantType: String,
        @Field("redirect_uri") redirectUri: String,
        @Field("code") code: String
    ): TokenResponse
}