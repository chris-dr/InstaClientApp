package com.drevnitskaya.instaclientapp.data.remote.api.content

import com.drevnitskaya.instaclientapp.data.remote.api.DataResponse
import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import retrofit2.http.GET

interface InstaContentApiInterface {
    @GET("users/self/")
    suspend fun getProfile(): DataResponse<Profile>
}