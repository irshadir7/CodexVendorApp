package com.invex.ventorapp.main.accessToken.network


import com.google.gson.JsonObject
import com.invex.ventorapp.main.accessToken.model.AccessTokenModel
import retrofit2.http.*

interface AccessTokenApi {
    @FormUrlEncoded
    @POST("api/v1/authentication/oauth2/token")
    suspend fun getRefreshToken(@Header("client_id") client_id:String,@Header("client_secret") client_secret:String,@Header("grant_type") grant_type:String,): AccessTokenModel
}