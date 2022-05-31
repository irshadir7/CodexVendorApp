package com.codex.ventorapp.foundatiion.utilz

import com.codex.ventorapp.main.model.ErrorDataModel
import java.lang.Exception

sealed class DataState<out R> {
    data class Success<out T>(val data: T) : DataState<T>()
    data class ServerError(val response: ErrorDataModel) : DataState<Nothing>()
    data class Error(val exception: Exception) : DataState<Nothing>()
    object Loading : DataState<Nothing>()
    data class Progress(val progress: Int): DataState<Nothing>()
}