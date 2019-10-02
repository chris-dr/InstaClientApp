package com.drevnitskaya.instaclientapp.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.drevnitskaya.instaclientapp.domain.auth.ComposeAuthUrlUseCase
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL

class LoginWebViewModel(composeAuthUrlUseCase: ComposeAuthUrlUseCase) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val loadLoginForm = MutableLiveData<String>()
    val showLoginForm = MutableLiveData<Boolean>()
    private var redirectUrl: String? = null

    init {
        showProgress.value = true
        val authUrl = composeAuthUrlUseCase.execute()
        loadLoginForm.value = authUrl
    }

    fun handleAuthUrl(redirectUrl: String?) {
        this.redirectUrl = redirectUrl
        if (redirectUrl?.startsWith(AUTH_REDIRECT_URL) == true) {
            //TODO: Handle redirect, get the code!!!
        }
    }

    fun onLoadFormFinished(url: String?) {
        if (redirectUrl == url) {
            showProgress.value = false
            showLoginForm.value = true
        }
    }

    fun handleError() {

    }
}