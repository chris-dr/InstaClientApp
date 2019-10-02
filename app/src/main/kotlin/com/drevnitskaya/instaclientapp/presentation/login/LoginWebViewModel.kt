package com.drevnitskaya.instaclientapp.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
import com.drevnitskaya.instaclientapp.domain.auth.ComposeAuthUrlUseCase
import com.drevnitskaya.instaclientapp.domain.auth.GetAccessTokenUseCase
import com.drevnitskaya.instaclientapp.domain.auth.ParseAuthCodeUseCase
import com.drevnitskaya.instaclientapp.framework.api.AUTH_REDIRECT_URL
import com.drevnitskaya.instaclientapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class LoginWebViewModel(
    composeAuthUrlUseCase: ComposeAuthUrlUseCase,
    private val parseAuthCodeUseCase: ParseAuthCodeUseCase,
    private val getAccessTokenUseCase: GetAccessTokenUseCase
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val loadLoginForm = MutableLiveData<String>()
    val showLoginForm = MutableLiveData<Boolean>()
    //todo: not here
    val openProfile = SingleLiveEvent<Unit>()
    private var redirectUrl: String? = null

    init {
        showProgress.value = true
        val authUrl = composeAuthUrlUseCase.execute()
        loadLoginForm.value = authUrl
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
                        TODO("Handle this error later")
                    }
                }
            }
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

    private suspend fun getToken(authCode: String?) {
        authCode?.let { code ->
            when (getAccessTokenUseCase.execute(code)) {
                is UseCaseResult.Complete -> {
                    //TODO: Open profile screen
                    openProfile.call()
                }
                is UseCaseResult.Error -> {
                    TODO("Handle error")
                }
                else -> {
                }
            }
        } ?: run {
            TODO("Handle error")
        }
    }
}