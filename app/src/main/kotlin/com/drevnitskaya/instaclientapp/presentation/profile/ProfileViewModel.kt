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
import kotlinx.coroutines.launch

private const val VISIBLE_PART_FROM_ITEM_HEIGHT_PERCENT = 0.85

class ProfileViewModel(
    private val networkStateProvider: NetworkStateProvider,
    private val getProfileUseCase: GetProfileUseCase,
    private val loadFeedUseCase: LoadInitialFeedUseCase,
    private val loadMoreFeedUseCase: LoadMoreFeedUseCase,
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
    val showCachedDataMessage = SingleLiveEvent<Unit>()
    private var nextFeedUrl: String? = null

    init {
        loadProfileContent()
    }

    fun loadProfileContent() {
        viewModelScope.launch {
            showErrorState.value = false
            showProgress.value = true
            val profileAsync = async { getProfileUseCase.execute() }
            val feedAsync = async { loadFeedUseCase.execute() }

            val profileResult = profileAsync.await()
            val feedResult = feedAsync.await()

            handleProfileResult(result = profileResult)
            handleFeedResult(result = feedResult)
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

    fun logout() {
        viewModelScope.launch {
            when (logoutUseCase.execute()) {
                is Result.Complete -> openLogin.call()
                is Result.Error -> showLogoutFailed.call()
            }
        }
    }

    private fun handleProfileResult(result: Result<ProfileWrapper>) {
        showProgress.value = false
        when (result) {
            is Result.Success -> {
                val profileWrapper = result.data
                showUserInfo.value = profileWrapper?.profile
                if (profileWrapper?.fromCache == true) {
                    showCachedDataMessage.call()
                }
            }
            is Result.Error -> {
                //todo: remove
                result.exception.printStackTrace()

                showErrorState.value = true
            }
        }
    }

    private fun handleFeedResult(result: Result<FeedWrapper>) {
        when (result) {
            is Result.Success -> {
                val feedWrapper = result.data

                val feed = result.data?.feed
                //TODO: Check that feed is not empty!
                showFeed.value = feed

                if (feedWrapper?.fromCache == false) {
                    nextFeedUrl = feedWrapper.nextUrl
                }
            }
            is Result.Error -> {
                showProgress.value = false
                //TODO: make some state for feed only!
//                showErrorState.value = true
            }

        }
    }
}