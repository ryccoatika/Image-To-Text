package com.ryccoatika.imagetotext.core.data

sealed class Resource<out R> {
    object Loading: Resource<Nothing>()
    object Empty: Resource<Nothing>()
    data class Success<T>(val data: T): Resource<T>()
    data class Error(val message: String): Resource<Nothing>()
}