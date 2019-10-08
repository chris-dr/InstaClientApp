package com.drevnitskaya.instaclientapp.data.source.local

private const val PREF_KEY_ACCESS_TOKEN = "insta_client.access_token"

interface TokenLocalDataSource {
    var token: String
}

class TokenLocalDataSourceImpl(
    private val prefsProvider: PreferenceProvider
) : TokenLocalDataSource {
    override var token: String
        set(value) = prefsProvider.setPreference(PREF_KEY_ACCESS_TOKEN, value)
        get() = prefsProvider.getPreference(PREF_KEY_ACCESS_TOKEN, "")
}