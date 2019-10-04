package com.drevnitskaya.instaclientapp.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
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
    val showErrorState = MutableLiveData<Boolean>()
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
                    is UseCaseResult.Success -> {
                        val authCode = authCodeResult.data
                        getToken(authCode)
                    }
                    is UseCaseResult.Error -> {
                        showErrorState.value = true
                    }
                }
            }
        }
    }

    fun onLoadFormFinished(url: String?) {
        if (redirectUrl == url) {
            showProgress.value = false
            if (showErrorState.value != true) {
                showLoginForm.value = true
            }
        }
    }

    fun handleError() {
        showProgress.value = false
        showLoginForm.value = false
        showErrorState.value = true
    }

    fun reloadAuthForm() {
        if (networkStateProvider.isNetworkAvailable()) {
            showErrorState.value = false
            showProgress.value = true
            loadAuthForm()
        } else {
            showErrorState.value = true
        }
    }

    private fun loadAuthForm() {
        val authUrl = composeAuthUrlUseCase.execute()
        loadLoginForm.value = authUrl
    }

    private suspend fun getToken(authCode: String?) {
        authCode?.let { code ->
            when (getAccessTokenUseCase.execute(code)) {
                is UseCaseResult.Complete -> openProfile.call()
                is UseCaseResult.Error -> showErrorState.value = true
                else -> {
                    //do nothing;
                }
            }
        } ?: run {
            showErrorState.value = true
        }
    }
}