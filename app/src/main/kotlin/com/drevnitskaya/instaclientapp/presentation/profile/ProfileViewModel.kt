package com.drevnitskaya.instaclientapp.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.data.remote.api.InstaMedia
import com.drevnitskaya.instaclientapp.data.remote.api.Profile
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.GetRemoteMediaUseCase
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val getMediaUseCase: GetRemoteMediaUseCase
) : ViewModel() {
    val showProgress = MutableLiveData<Boolean>()
    val showUserInfo = MutableLiveData<Profile>()
    val showFeed = MutableLiveData<List<InstaMedia>>()

    init {
        viewModelScope.launch {
            showProgress.value = true
            val profileAsync = async { getProfileUseCase.execute() }
            val mediaAsync = async { getMediaUseCase.execute() }

            val profileResult = profileAsync.await()
            val mediaResult = mediaAsync.await()

            when (profileResult) {
                is UseCaseResult.Success -> {
                    val profile = profileResult.data
                    showProgress.value = false
                    showUserInfo.postValue(profile)
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
                    showFeed.postValue(instaMedia)
                }
                is UseCaseResult.Error -> {
                    mediaResult.exception.printStackTrace()
                }
            }
        }
    }
}