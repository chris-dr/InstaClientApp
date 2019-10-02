package com.drevnitskaya.instaclientapp.data.repository.auth

import com.drevnitskaya.instaclientapp.data.local.PreferenceProvider
import kotlin.properties.Delegates

private const val PREF_KEY_ACCESS_TOKEN = "insta_client.access_token"

interface AuthLocalRepository {
    var token: String
}

class AuthLocalRepositoryImpl(
    private val localDataSource: PreferenceProvider
) : AuthLocalRepository {
    override var token: String by Delegates.observable("") { _, _, newValue ->
        localDataSource.setPreference(PREF_KEY_ACCESS_TOKEN, newValue)
    }

}