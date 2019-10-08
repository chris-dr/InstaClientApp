package com.drevnitskaya.instaclientapp.data

sealed class Result<out T : Any?> {
    data class Success<out T : Any>(val data: T?) : Result<T>()
    object Complete : Result<Nothing>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}