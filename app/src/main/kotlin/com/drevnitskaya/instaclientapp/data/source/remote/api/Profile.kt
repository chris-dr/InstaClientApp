package com.drevnitskaya.instaclientapp.data.source.remote.api

import com.google.gson.annotations.SerializedName

data class Profile(
    var id: String? = null,
    var username: String? = null,
    @SerializedName("full_name")
    var fullName: String? = null,
    @SerializedName("profile_picture")
    var profilePictureUrl: String? = null,
    var bio: String? = null,
    var counts: Count? = null
)

data class Count(
    var media: Int,
    var follows: Int,
    @SerializedName("followed_by")
    var followedBy: Int
)