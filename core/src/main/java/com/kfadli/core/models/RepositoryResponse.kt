package com.kfadli.core.models

sealed class RepositoryResponse<out T : Any, out U : Any> {
    data class Success<T : Any>(val response: T?) : RepositoryResponse<T, Nothing>()
    data class Failure<U : Any>(val throwable: Throwable) : RepositoryResponse<Nothing, U>()
}