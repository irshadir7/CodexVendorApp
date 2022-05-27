package com.codex.ventorapp.main.login.network

import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.google.gson.JsonObject
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginApi {
    @FormUrlEncoded
    @POST("api/v1/api/user_login")
    suspend fun getLogin(@Field("login") login:String, @Field("password") password:String): JsonObject
}