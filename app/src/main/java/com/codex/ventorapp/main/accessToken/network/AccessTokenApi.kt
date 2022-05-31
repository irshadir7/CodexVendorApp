package com.codex.ventorapp.main.accessToken.network


import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import retrofit2.http.*

interface AccessTokenApis {
    @FormUrlEncoded
    @POST("api/v1/authentication/oauth2/token")
    suspend fun getRefreshToken(@Field("client_id") client_id:String, @Field("client_secret") client_secret:String, @Field("grant_type") grant_type:String,): AccessTokenModel
}