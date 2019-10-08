package com.drevnitskaya.instaclientapp.framework.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.drevnitskaya.instaclientapp.data.entities.Profile
import com.drevnitskaya.instaclientapp.data.source.local.dao.ProfileLocalDataSource

const val DATA_BASE_NAME = "insta_app_room_db"
const val TABLE_NAME_PROFILE ="profile"

@Database(entities = [Profile::class], version = 1)
abstract class InstaAppDataBase : RoomDatabase() {
    abstract fun profileDao(): ProfileLocalDataSource
}