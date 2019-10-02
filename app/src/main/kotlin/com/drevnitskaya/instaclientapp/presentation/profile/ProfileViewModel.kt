package com.drevnitskaya.instaclientapp.presentation.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.drevnitskaya.instaclientapp.domain.GetProfileUseCase
import com.drevnitskaya.instaclientapp.domain.UseCaseResult
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase
) : ViewModel() {
    val test = MutableLiveData<Unit>()

    init {
        viewModelScope.launch {
            when (val result = getProfileUseCase.execute()) {
                is UseCaseResult.Success -> {
                    val profile = result.data
                    Log.d(javaClass.simpleName, "Profile is: $profile")
                }
                is UseCaseResult.Error -> {
                    //TODO: Handle Error!
                    result.exception.printStackTrace()
                }
            }
        }
    }

    fun testFun() {

    }

}