package com.ap.moviepocket.domain

sealed class UseCaseResult<out R> {

    data class Success<out T>(val data: T) : UseCaseResult<T>()
    data class Error(val exception: Exception) : UseCaseResult<Nothing>()
    object Loading : UseCaseResult<Nothing>()

}

val UseCaseResult<*>.succeeded
    get() = this is UseCaseResult.Success && data != null

fun <T> UseCaseResult<T>.successOr(fallback: T): T {
    return (this as? UseCaseResult.Success<T>)?.data ?: fallback
}

val <T> UseCaseResult<T>.data: T?
    get() = (this as? UseCaseResult.Success)?.data
