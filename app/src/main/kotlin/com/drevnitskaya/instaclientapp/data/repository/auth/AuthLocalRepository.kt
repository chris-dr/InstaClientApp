package com.drevnitskaya.instaclientapp.data.repository.auth

import com.drevnitskaya.instaclientapp.data.source.local.PreferenceProvider

private const val PREF_KEY_ACCESS_TOKEN = "insta_client.access_token"

interface AuthLocalRepository {
    var token: String
}

class AuthLocalRepositoryImpl(
    private val localDataSource: PreferenceProvider
) : AuthLocalRepository {
    override var token: String
        set(value) = localDataSource.setPreference(PREF_KEY_ACCESS_TOKEN, value)
        get() = localDataSource.getPreference(PREF_KEY_ACCESS_TOKEN, "")
}