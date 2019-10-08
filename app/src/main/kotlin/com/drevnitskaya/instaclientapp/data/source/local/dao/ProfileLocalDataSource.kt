package com.drevnitskaya.instaclientapp.data.source.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.framework.db.TABLE_NAME_PROFILE

@Dao
interface ProfileLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: Profile)

    @Query("SELECT * FROM $TABLE_NAME_PROFILE")
    suspend fun getProfile(): List<Profile>
}