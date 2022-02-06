package com.demo.end.main.network

import com.demo.end.main.model.EndProductsModel
import retrofit2.http.GET
import retrofit2.http.Query

interface EndEcommerceApi {


    @GET("media/catalog/android_test_example.json")
    suspend fun getProducts() : EndProductsModel

    @GET("media/catalog/example.json")
    suspend fun getMoreProducts() : EndProductsModel
}