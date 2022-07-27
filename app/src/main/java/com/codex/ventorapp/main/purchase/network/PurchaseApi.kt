package com.codex.ventorapp.main.purchase.network

import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.google.gson.JsonObject
import retrofit2.http.*

interface PurchaseApi {


    @GET("api/v1/list_po")
    suspend fun getPurchaseList(@Header("Authorization") token: String): JsonObject

    @GET("api/v1/list/customer")
    suspend fun getAllCustomer(@Header("Authorization") token: String): JsonObject
}