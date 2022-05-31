package com.codex.ventorapp.main.signup.network

import com.google.gson.JsonObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface SignupApi {
    @FormUrlEncoded
    @POST("api/v1/api/register_login")
    suspend fun getSignup(@Header("Authorization") token:String,@Field("email") email:String, @Field("name") name:String,@Field("password") password:String): JsonObject
}