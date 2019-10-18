package com.drevnitskaya.instaclientapp.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.domain.LoadMoreFeedUseCase
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.LoadInitialFeedUseCase
import com.drevnitskaya.instaclientapp.data.Result
import com.drevnitskaya.instaclientapp.data.entities.*
import com.drevnitskaya.instaclientapp.domain.auth.LogoutUseCase
import com.drevnitskaya.instaclientapp.utils.NetworkStateProvider
import com.drevnitskaya.instaclientapp.utils.SingleLiveEvent
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private const val VISIBLE_PART_FROM_ITEM_HEIGHT_PERCENT = 0.7

class ProfileViewModel(
    private val networkStateProvider: NetworkStateProvider,
    private val getProfileUseCase: GetProfileUseCase,
    private val loadInitialFeedUseCase: LoadInitialFeedUseCase,
    private val loadMoreFeedUseCase: LoadMoreFeedUseCase,
    private val logoutUseCase: LogoutUseCase
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val showRefreshing = MutableLiveData(false)
    val showRefreshingFailedState = SingleLiveEvent<ErrorHolder>()
    val showUserInfo = MutableLiveData<Profile>()
    val showFeed = MutableLiveData<List<FeedItem>>()
    val showEmptyFeedState = MutableLiveData<Boolean>()
    val showFeedErrorState = MutableLiveData<Boolean>()
    val openLogin = SingleLiveEvent<Nothing>()
    val showLogoutFailed = SingleLiveEvent<Nothing>()
    val showErrorState = MutableLiveData<ErrorHolder>()
    val showLoadMoreFeed = MutableLiveData(false)
    val showLoadMoreError = MutableLiveData<Boolean>()
    val showOfflineModeMessage = SingleLiveEvent<Unit>()
    private var nextFeedUrl: String? = null

    init {
        loadProfileContent()
    }

    fun loadProfileContent(isRefreshing: Boolean = false) {
        viewModelScope.launch {
            showErrorState.value = null
            if (isRefreshing.not()) {
                showProgress.value = true
            }
            val profileAsync = async { getProfileUseCase.execute() }
            val feedAsync = async { loadInitialFeedUseCase.execute() }
            val profileResult = profileAsync.await()
            val feedResult = feedAsync.await()
            handleProfileResult(result = profileResult, isRefreshing = isRefreshing)
            handleFeedResult(result = feedResult, isRefreshing = isRefreshing)
        }
    }

    fun loadMoreFeedIfNeeded(localVisibleRectBottomPosition: Int, itemHeight: Int) {
        if (localVisibleRectBottomPosition >= VISIBLE_PART_FROM_ITEM_HEIGHT_PERCENT * itemHeight
            && showLoadMoreFeed.value == false
            && nextFeedUrl.isNullOrBlank().not()
            && showRefreshing.value == false
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
            when (val result = loadMoreFeedUseCase.execute(nextFeedUrl!!)) {
                is Result.Success -> {
                    val feedWrapper = result.data
                    val newItems = feedWrapper?.feed
                    if (newItems.isNullOrEmpty().not()) {
                        val currFeed = showFeed.value?.toMutableList()
                        val tempList = currFeed?.apply {
                            addAll(newItems!!)
                        }
                        nextFeedUrl = feedWrapper?.nextUrl
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

    fun refreshContent() {
        if (networkStateProvider.isNetworkAvailable().not()) {
            showRefreshing.value = false
            showRefreshingFailedState.value = ErrorHolder.NetworkError()
            return
        }
        if (showLoadMoreFeed.value == true) {
            showRefreshing.value = false
            return
        }
        nextFeedUrl = null
        showRefreshing.value = true
        loadProfileContent(isRefreshing = true)
    }

    fun logout() {
        viewModelScope.launch {
            when (logoutUseCase.execute()) {
                is Result.Complete -> openLogin.call()
                is Result.Error -> showLogoutFailed.call()
            }
        }
    }

    private suspend fun handleProfileResult(result: Result<ProfileWrapper>, isRefreshing: Boolean) {
        when (result) {
            is Result.Success -> {
                val profileWrapper = result.data
                showUserInfo.value = profileWrapper?.profile
                if (profileWrapper?.fromCache == true) {
                    delay(150)
                    showOfflineModeMessage.call()
                }
            }
            is Result.Error -> {
                if (isRefreshing.not()) {
                    showErrorState.value = ErrorHolder.GeneralError()
                }
            }
        }
    }

    private fun handleFeedResult(result: Result<FeedWrapper>, isRefreshing: Boolean) {
        showProgress.value = false
        showRefreshing.value = false
        when (result) {
            is Result.Success -> {
                val feedWrapper = result.data
                val feed = result.data?.feed
                if (feed.isNullOrEmpty().not()) {
                    showFeed.value = feed
                    if (feedWrapper?.fromCache == false) {
                        nextFeedUrl = feedWrapper.nextUrl
                    }
                } else {
                    if (isRefreshing.not()) {
                        showEmptyFeedState.value = true
                    } else {
                        showRefreshingFailedState.value = ErrorHolder.RefreshingError()
                    }
                }
            }
            is Result.Error -> {
                if (isRefreshing.not()) {
                    showFeedErrorState.value = true
                } else {
                    showRefreshingFailedState.value = ErrorHolder.RefreshingError()
                }
            }
        }
    }
}