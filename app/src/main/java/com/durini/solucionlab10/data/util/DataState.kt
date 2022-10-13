package com.durini.solucionlab10.data.util

sealed class DataState<out R>{
    // Success("Character)
    data class Success<out T>(val data: T) : DataState<T>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()

}
