package com.drevnitskaya.instaclientapp.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.ErrorHolder
import com.drevnitskaya.instaclientapp.domain.auth.ComposeAuthUrlUseCase
import com.drevnitskaya.instaclientapp.domain.auth.GetAccessTokenUseCase
import com.drevnitskaya.instaclientapp.domain.auth.ParseAuthCodeUseCase
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL
import com.drevnitskaya.instaclientapp.utils.NetworkStateProvider
import com.drevnitskaya.instaclientapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LoginWebViewModel(
    private val networkStateProvider: NetworkStateProvider,
    private val composeAuthUrlUseCase: ComposeAuthUrlUseCase,
    private val parseAuthCodeUseCase: ParseAuthCodeUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val loadLoginForm = MutableLiveData<String>()
    val showLoginForm = MutableLiveData<Boolean>()
    val showErrorState = MutableLiveData<ErrorHolder>()
    val openProfile = SingleLiveEvent<Unit>()
    private var redirectUrl: String? = null

    init {
        if (networkStateProvider.isNetworkAvailable()) {
            showProgress.value = true
            loadAuthForm()
        } else {
            handleError()
        }
    }

    fun handleAuthUrl(redirectUrl: String?) {
        this.redirectUrl = redirectUrl
        if (redirectUrl?.startsWith(AUTH_REDIRECT_URL) == true) {
            viewModelScope.launch {
                when (val authCodeResult = parseAuthCodeUseCase.execute(redirectUrl)) {
                    is Result.Success<String> -> {
                        val authCode = authCodeResult.data
                        getToken(authCode)
                    }
                    is Result.Error -> {
                        showErrorState.value = ErrorHolder.GeneralError()
                    }
                }
            }
        }
    }

    fun onLoadFormFinished(url: String?) {
        if (redirectUrl == url) {
            showProgress.value = false
            if (showErrorState.value == null) {
                showLoginForm.value = true
            }
        }
    }

    fun handleError() {
        showProgress.value = false
        showLoginForm.value = false
        showErrorState.value = ErrorHolder.NetworkError()
    }

    fun reloadAuthForm() {
        if (networkStateProvider.isNetworkAvailable()) {
            showErrorState.value = null
            showProgress.value = true
            loadAuthForm()
        } else {
            showErrorState.value = ErrorHolder.NetworkError()
        }
    }

    private fun loadAuthForm() {
        val authUrl = composeAuthUrlUseCase.execute()
        loadLoginForm.value = authUrl
    }

    private suspend fun getToken(authCode: String?) {
        authCode?.let { code ->
            when (getAccessTokenUseCase.execute(code)) {
                is Result.Complete -> openProfile.call()
                is Result.Error -> showErrorState.value = ErrorHolder.GeneralError()
                else -> {
                    //do nothing;
                }
            }
        } ?: run {
            showErrorState.value = ErrorHolder.GeneralError()
        }
    }
}