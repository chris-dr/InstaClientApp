package com.drevnitskaya.instaclientapp.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drevnitskaya.instaclientapp.framework.db.TABLE_NAME_FEED
import com.google.gson.annotations.SerializedName

data class Image(
    var url: String? = null,
    var width: Int? = null,
    var height: Int? = null
)

data class Images(
    @SerializedName("standard_resolution")
    @Embedded
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

@Entity(tableName = TABLE_NAME_FEED)
data class FeedItem(
    @PrimaryKey
    var id: String = "",
    var type: String? = null,
    @SerializedName("created_time")
    var createdTime: String? = null,
    @Embedded
    var caption: Caption? = null,
    @Embedded
    var images: Images? = null,
    @Embedded
    var likes: Likes? = null,
    @Embedded
    var location: Location? = null
)

data class FeedWrapper(
    var feed: List<FeedItem>? = null,
    var nextUrl: String? = null,
    var fromCache: Boolean = false
)