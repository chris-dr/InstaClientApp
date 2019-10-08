package com.drevnitskaya.instaclientapp.data.source.remote

import com.drevnitskaya.instaclientapp.data.entities.DataResponse
import com.drevnitskaya.instaclientapp.data.entities.FeedItem
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.data.entities.TokenResponse
import retrofit2.http.*

interface RemoteDataSource {

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
    suspend fun getInitialFeed(
        @Query("access_token") token: String,
        @Query("count") count: Int
    ): DataResponse<List<FeedItem>>

    @GET
    suspend fun getMoreFeed(@Url nextUrl: String): DataResponse<List<FeedItem>>
}