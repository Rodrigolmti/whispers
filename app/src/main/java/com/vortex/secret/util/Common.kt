package com.vortex.secret.util

typealias Block = suspend () -> Unit

sealed class Result<out T : Any> {
    class Success<T : Any>(val data: T) : Result<T>()
    class Error(val error: Throwable) : Result<Nothing>()
}