package com.drevnitskaya.instaclientapp.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.drevnitskaya.instaclientapp.framework.db.TABLE_NAME_PROFILE
import com.google.gson.annotations.SerializedName

@Entity(tableName = TABLE_NAME_PROFILE)
data class Profile(
    @PrimaryKey
    var id: String = "",
    var username: String? = null,
    @SerializedName("full_name")
    var fullName: String? = null,
    @SerializedName("profile_picture")
    var profilePictureUrl: String? = null,
    var bio: String? = null,
    @Embedded
    var counts: Count? = null
)

data class Count(
    var media: Int,
    var follows: Int,
    @SerializedName("followed_by")
    var followedBy: Int
)

const val DATA_SOURCE_REMOTE = 0
const val DATA_SOURCE_LOCAL = 1

data class ProfileWrapper(
    var profile: Profile? = null,
    var dataSource: Int = DATA_SOURCE_REMOTE
)