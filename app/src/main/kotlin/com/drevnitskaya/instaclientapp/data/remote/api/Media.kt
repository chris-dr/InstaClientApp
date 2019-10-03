package com.drevnitskaya.instaclientapp.data.remote.api

import com.google.gson.annotations.SerializedName

data class Image(
    var url: String? = null,
    var width: Int? = null,
    var height: Int? = null
)

data class Images(
    @SerializedName("low_resolution")
    var lowResolutionImg: Image? = null,
    @SerializedName("thumbnail")
    var thumbnailImg: Image? = null,
    @SerializedName("standard_resolution")
    var standardResolutionImg: Image? = null
)

data class Caption(
    var text: String? = null
)

data class Location(
    var name: String? = null
)

data class Likes(
    var count: Int? = 0
)

data class InstaMedia(
    var id: String? = null,
    var type: String? = null,
    @SerializedName("created_time")
    var createdTime: String? = null,
    var caption: Caption? = null,
    var images: Images? = null,
    var likes: Likes? = null,
    var location: Location? = null
)