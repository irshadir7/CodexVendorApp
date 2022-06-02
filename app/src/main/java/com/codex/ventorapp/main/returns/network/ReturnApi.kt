package com.codex.ventorapp.main.returns.network


import com.google.gson.JsonObject
import retrofit2.http.*

interface ReturnApi {

    @GET("api/v1/list_picking/returns")
    suspend fun getReturn(@Header("Authorization") token:String,@Query("partner_id") partner_id:String):JsonObject
}