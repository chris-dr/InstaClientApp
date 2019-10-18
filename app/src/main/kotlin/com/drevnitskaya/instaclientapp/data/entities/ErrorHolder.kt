package com.drevnitskaya.instaclientapp.data.entities

import androidx.annotation.StringRes
import com.drevnitskaya.instaclientapp.R

sealed class ErrorHolder(@StringRes val errorMsgRes: Int) {
    class NetworkError : ErrorHolder(R.string.shared_noNetworkConnMsg)
    class GeneralError : ErrorHolder(R.string.shared_generalErrorMsg)
    class RefreshingError : ErrorHolder(R.string.profile_refreshingError)
}
