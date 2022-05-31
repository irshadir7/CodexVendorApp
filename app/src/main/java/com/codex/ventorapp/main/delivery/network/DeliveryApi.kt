package com.codex.ventorapp.main.delivery.network

import com.google.gson.JsonObject
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface DeliveryApi {
    @GET("api/v1/list_picking/outgoing")
    suspend fun getDelivery(@Header("Authorization") token:String, @Query("partner_id") partner_id:String): JsonObject
}