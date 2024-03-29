package com.drevnitskaya.instaclientapp.data.source.local.dao

import androidx.room.*
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.framework.db.TABLE_NAME_PROFILE

@Dao
interface ProfileLocalDataSource {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveProfile(profile: Profile)

    @Query("SELECT * FROM $TABLE_NAME_PROFILE")
    suspend fun getProfile(): List<Profile>

    @Query("DELETE FROM $TABLE_NAME_PROFILE")
    suspend fun clear()
}