package com.drevnitskaya.instaclientapp.utils

import android.content.Context
import android.net.ConnectivityManager

interface NetworkStateProvider {
    fun isNetworkAvailable(): Boolean
}

class NetworkStateProviderImpl(private val context: Context) : NetworkStateProvider {

    override fun isNetworkAvailable(): Boolean {
        val connMgr = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connMgr.activeNetworkInfo?.isConnected ?: false
    }
}