package com.drevnitskaya.instaclientapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.drevnitskaya.instaclientapp.data.entities.FeedItem
import com.drevnitskaya.instaclientapp.framework.db.TABLE_NAME_FEED

@Dao
interface FeedLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFeed(feed: List<FeedItem>)

    @Query("SELECT * FROM $TABLE_NAME_FEED")
    suspend fun getFeed(): List<FeedItem>
}