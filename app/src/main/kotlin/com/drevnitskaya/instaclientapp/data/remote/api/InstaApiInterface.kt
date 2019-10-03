package com.drevnitskaya.instaclientapp.data.remote.api

import com.drevnitskaya.instaclientapp.data.remote.api.auth.entities.TokenResponse
import retrofit2.http.*

interface InstaApiInterface {

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


    @GET("v1/users/self/")
    suspend fun getProfile(@Query("access_token") token: String): DataResponse<Profile>

    @GET("v1/users/self/media/recent/")
    suspend fun getMedia(
        @Query("access_token") token: String,
        @Query("max_id") maxId: Int,
        @Query("min_id") minId: Int,
        @Query("count") count: Int
    ): DataResponse<List<InstaMedia>>
}