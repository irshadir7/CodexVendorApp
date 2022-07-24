package com.codex.ventorapp.main.inventory_adjustment.network

import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.google.gson.JsonObject
import retrofit2.http.*

interface InventoryAdjustmentApi {

    @GET("api/v1/inventory_adjustment/list")
    suspend fun getAdjustList(@Header("Authorization") token: String): JsonObject

    @Headers("Content-Type: application/json")
    @GET("api/v1/product/barcode/scan/{id}")
    suspend fun getBarCodeProduct(
        @Path("id") id: String,
        @Header("Authorization") token: String,
        @Query("location_id") location_id: String,
    ): JsonObject


    @Headers("Content-Type: application/json")
    @POST("api/v1/inventory_adjustment")
    suspend fun getInventoryAdjustment(
        @Header("Authorization") token: String,
        @Query("product_id") product_id: String,
        @Query("location_id") location_id: String,
        @Query("stock_quant_id") stock_quantity_id: String,
        @Query("inventory_quantity") inventory_quantity: String
    ): JsonObject
}