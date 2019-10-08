package com.drevnitskaya.instaclientapp.data.repository

import android.util.Log
import com.drevnitskaya.instaclientapp.data.source.remote.RemoteDataSource
import com.drevnitskaya.instaclientapp.data.entities.ProfileWrapper
import com.drevnitskaya.instaclientapp.data.source.local.TokenLocalDataSource
import com.drevnitskaya.instaclientapp.data.source.local.dao.ProfileLocalDataSource

interface ProfileRepository {
    suspend fun getProfile(forceUpdate: Boolean = false): ProfileWrapper

    suspend fun removeProfile()
}

class ProfileRepositoryImpl(
    private val tokenLocalDataSource: TokenLocalDataSource,
    private val profileRemoteDataSource: RemoteDataSource,
    private val profileLocalDataSource: ProfileLocalDataSource
) : ProfileRepository {

    override suspend fun getProfile(forceUpdate: Boolean): ProfileWrapper {
        val remoteProfile = try {
            val token = tokenLocalDataSource.token
            profileRemoteDataSource.getProfile(token).data
        } catch (ex: Exception) {
            null
        }

        if (remoteProfile == null) {
            Log.w(javaClass.canonicalName, "Remote data source fetch failed")
        } else {
            profileLocalDataSource.apply {
                clear()
                saveProfile(remoteProfile)
            }
            return ProfileWrapper(profile = remoteProfile)
        }

        if (forceUpdate) {
            throw Exception("Refresh failed")
        }

        val localProfile = try {
            profileLocalDataSource.getProfile()
        } catch (ex: Exception) {
            null
        }

        if (localProfile != null) {
            return ProfileWrapper(profile = localProfile[0], fromCache = true)
        } else {
            throw Exception("Error fetching from remote and local")
        }
    }

    override suspend fun removeProfile() {
        profileLocalDataSource.clear()
    }
}