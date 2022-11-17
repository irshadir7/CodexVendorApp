package com.codex.ventorapp.main.onboarding.repo

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.model.ErrorDataModel
import com.codex.ventorapp.main.onboarding.model.DatabaseList
import com.codex.ventorapp.main.onboarding.network.DatabaseApi
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.create
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject


class DatabaseRepo
@Inject
constructor(private val databaseApi: DatabaseApi) {
    fun getDatabase(): Flow<DataState<DatabaseList>> = flow {
        try {
            val mediaType: MediaType = "application/json".toMediaTypeOrNull()!!
            val body: RequestBody = "{}".toRequestBody(mediaType)
            val response: DatabaseList = databaseApi.getDatabase(body)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }
}
