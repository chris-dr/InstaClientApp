package com.drevnitskaya.instaclientapp.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.domain.auth.CheckAuthStateUseCase
import com.drevnitskaya.instaclientapp.utils.SingleLiveEvent
import kotlinx.coroutines.launch

class SplashViewModel(
    private val checkAuthStateUseCase: CheckAuthStateUseCase
) : ViewModel() {
    val openLogin = SingleLiveEvent<Unit>()
    val openProfile = SingleLiveEvent<Unit>()

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        viewModelScope.launch {
            val hasUser = checkAuthStateUseCase.execute()
            if (hasUser) openProfile.call() else openLogin.call()
        }
    }
}