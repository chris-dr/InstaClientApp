package com.drevnitskaya.instaclientapp.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.GetRemoteFeedUseCase
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCase
import com.drevnitskaya.instaclientapp.utils.SingleLiveEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getFeedUseCase: GetRemoteFeedUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val showUserInfo = MutableLiveData<Profile>()
    val showFeed = MutableLiveData<List<FeedItem>>()
    val openLogin = SingleLiveEvent<Nothing>()
    val showLogoutFailed = SingleLiveEvent<Nothing>()
    val showErrorState = MutableLiveData<Boolean>()

    init {
        viewModelScope.launch {
            showProgress.value = true
            val profileAsync = async { getProfileUseCase.execute() }
            val feedAsync = async { getFeedUseCase.execute() }

            val profileResult = profileAsync.await()
            val feedResult = feedAsync.await()

            if (profileResult is UseCaseResult.Success && feedResult is UseCaseResult.Success) {
                val profile = profileResult.data
                val feed = feedResult.data
                showProgress.value = false
                showUserInfo.value = profile
                //todo: handle feed, can be empty!
                showFeed.value = feed
            } else {
                showProgress.value = false
                showErrorState.value = true
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            when (logoutUseCase.execute()) {
                is UseCaseResult.Complete -> openLogin.call()
                is UseCaseResult.Error -> showLogoutFailed.call()
            }
        }
    }
}