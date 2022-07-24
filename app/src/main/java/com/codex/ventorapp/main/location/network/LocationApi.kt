package com.codex.ventorapp.main.location.network

import com.google.gson.JsonObject
import retrofit2.http.*

interface LocationApi {

    @GET("api/v1/list/location")
    suspend fun getLocationList(@Header("Authorization") token:String): JsonObject


}