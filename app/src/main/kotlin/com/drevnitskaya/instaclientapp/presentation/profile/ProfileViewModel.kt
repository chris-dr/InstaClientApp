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

    init {
        viewModelScope.launch {
            showProgress.value = true
            val profileAsync = async { getProfileUseCase.execute() }
            val feedAsync = async { getFeedUseCase.execute() }

            val profileResult = profileAsync.await()
            val mediaResult = feedAsync.await()

            when (profileResult) {
                is UseCaseResult.Success -> {
                    val profile = profileResult.data
                    showProgress.value = false
                    showUserInfo.value = profile
                }
                is UseCaseResult.Error -> {
                    //TODO: Handle Error!
                    profileResult.exception.printStackTrace()
                    showProgress.value = false
                }
            }
            when (mediaResult) {
                is UseCaseResult.Success -> {
                    val instaMedia = mediaResult.data
                    showFeed.value = instaMedia
                }
                is UseCaseResult.Error -> {
                    mediaResult.exception.printStackTrace()
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            showLogoutFailed.call()
//            when (logoutUseCase.execute()) {
//                is UseCaseResult.Complete -> openLogin.call()
//                is UseCaseResult.Error -> showLogoutFailed.call()
//            }
        }
    }
}