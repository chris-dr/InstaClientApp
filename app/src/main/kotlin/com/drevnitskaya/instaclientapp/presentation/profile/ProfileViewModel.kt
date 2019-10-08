package com.drevnitskaya.instaclientapp.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.data.remote.api.FeedItem
import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import com.drevnitskaya.instaclientapp.domain.GetMoreFeedUseCase
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.GetRemoteFeedUseCase
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCase
import com.drevnitskaya.instaclientapp.utils.NetworkStateProvider
import com.drevnitskaya.instaclientapp.utils.SingleLiveEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

private const val VISIBLE_PART_FROM_ITEM_HEIGHT_PERCENT = 0.85

class ProfileViewModel(
    private val networkStateProvider: NetworkStateProvider,
    private val getProfileUseCase: GetProfileUseCase,
    private val getFeedUseCase: GetRemoteFeedUseCase,
    private val getMoreFeedUseCase: GetMoreFeedUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val showUserInfo = MutableLiveData<Profile>()
    val showFeed = MutableLiveData<List<FeedItem>>()
    val openLogin = SingleLiveEvent<Nothing>()
    val showLogoutFailed = SingleLiveEvent<Nothing>()
    val showErrorState = MutableLiveData<Boolean>()
    val showLoadMoreFeed = MutableLiveData(false)
    val showLoadMoreError = MutableLiveData<Boolean>()
    private var nextFeedUrl: String? = null

    init {
        loadProfileContent()
    }

    fun loadProfileContent() {
        viewModelScope.launch {
            showErrorState.value = false
            showProgress.value = true
            val profileAsync = async { getProfileUseCase.execute() }
            val feedAsync = async { getFeedUseCase.execute() }

            val profileResult = profileAsync.await()
            val feedResult = feedAsync.await()

            if (profileResult is UseCaseResult.Success && feedResult is UseCaseResult.Success) {
                val profile = profileResult.data
                val feed = feedResult.data?.data
                nextFeedUrl = feedResult.data?.pagination?.nextUrl
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

    fun loadMoreFeedIfNeeded(localVisibleRectBottomPosition: Int, itemHeight: Int) {
        if (localVisibleRectBottomPosition >= VISIBLE_PART_FROM_ITEM_HEIGHT_PERCENT * itemHeight
            && showLoadMoreFeed.value == false
            && nextFeedUrl.isNullOrBlank().not()
        ) {
            loadMoreFeed()
        }
    }

    fun loadMoreFeed() {
        if (networkStateProvider.isNetworkAvailable().not()) {
            showLoadMoreError.value = true
            return
        }
        viewModelScope.launch {
            showLoadMoreError.value = false
            showLoadMoreFeed.value = true
            when (val result = getMoreFeedUseCase.execute(nextFeedUrl!!)) {
                is UseCaseResult.Success -> {
                    val newItems = result.data?.data

                    if (newItems.isNullOrEmpty().not()) {
                        val currFeed = showFeed.value?.toMutableList()
                        val tempList = currFeed?.apply {
                            addAll(newItems!!)
                        }
                        nextFeedUrl = result.data?.pagination?.nextUrl
                        showLoadMoreFeed.value = false
                        showFeed.value = tempList
                    }
                }
                else -> {
                    showLoadMoreFeed.value = false
                    showLoadMoreError.value = true
                }
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