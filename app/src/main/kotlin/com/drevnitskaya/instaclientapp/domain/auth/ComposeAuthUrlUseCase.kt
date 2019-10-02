package com.drevnitskaya.instaclientapp.domain.auth

import android.net.Uri
import com.drevnitskaya.instaclientapp.BuildConfig
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL
import com.drevnitskaya.instaclientapp.framework.api.INSTA_AUTHORIZE_URL

interface ComposeAuthUrlUseCase {
    fun execute(): String
}

class ComposeAuthUrlUseCaseImpl : ComposeAuthUrlUseCase {
    override fun execute(): String {
        return Uri.parse(INSTA_AUTHORIZE_URL).buildUpon()
            .appendQueryParameter("client_id", BuildConfig.INSTA_CLIENT_ID)
            .appendQueryParameter("redirect_uri", AUTH_REDIRECT_URL)
            .appendQueryParameter("response_type", "code")
            .build().toString()
    }
}