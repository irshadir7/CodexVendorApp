package com.codex.ventorapp.main.inventory_adjustment.network

import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.google.gson.JsonObject
import retrofit2.http.*

interface InventoryAdjustmentApi {

    @GET("api/v1/inventory_adjustment/list")
    suspend fun getAdjustList(@Header("Authorization") token:String): JsonObject
}