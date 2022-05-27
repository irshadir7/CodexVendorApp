package com.demo.ventorapp.main.accessToken.repository

import android.util.Log
import com.demo.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.accessToken.network.AccessTokenApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AccessTokenRepo
@Inject
constructor(private val accessTokenApi: AccessTokenApi){
    fun getAccessToken(client_id: String, client_secret: String,grant_type :String): Flow<DataState<AccessTokenModel>> = flow {
        try {
            val response:AccessTokenModel = accessTokenApi.getRefreshToken(client_id,client_secret,grant_type)
            emit(DataState.Success(response))
        } catch (e: Exception) {
            Log.i("Ex",e.toString())
            emit(DataState.Error(e))
        }
    }
}