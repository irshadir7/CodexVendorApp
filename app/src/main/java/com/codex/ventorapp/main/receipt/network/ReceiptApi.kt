package com.codex.ventorapp.main.receipt.network


import com.google.gson.JsonObject
import retrofit2.http.*

interface ReceiptApi {

    @GET("api/v1/list_picking/incoming")
    suspend fun getReceipt(@Header("Authorization") token:String,@Query("partner_id") partner_id:String):JsonObject
}