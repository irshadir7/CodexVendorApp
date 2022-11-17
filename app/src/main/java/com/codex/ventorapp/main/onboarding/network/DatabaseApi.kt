package com.codex.ventorapp.main.onboarding.network

import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.onboarding.model.DatabaseList
import com.google.gson.JsonObject
import okhttp3.RequestBody
import retrofit2.http.*

interface DatabaseApi {
    @Headers("Content-Type: application/json")
    @POST("web/database/list")
    suspend fun getDatabase(@Body body: RequestBody): DatabaseList
}