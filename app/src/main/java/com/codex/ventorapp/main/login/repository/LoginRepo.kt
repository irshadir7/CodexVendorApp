package com.codex.ventorapp.main.login.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.network.LoginApi
import com.codex.ventorapp.main.model.ErrorDataModel
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginRepo
@Inject
constructor(private val loginAPI: LoginApi){
    fun getLogin(db: String,login: String, password: String): Flow<DataState<LoginSuccessData>> = flow {
        try {
            val apiErrorModel: ErrorDataModel
            val response: JsonObject = loginAPI.getLogin(db,login,password)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: LoginSuccessData?
            Log.d("LoginResponse",db)
            if (statusJson == 200) {
                val accessToken: String = response.get("access_token").asString
                val message: String = response.get("message").asString
                val tokenType: String = response.get("token_type").asString
                val partnerId: String = response.get("partner_id").asString
                val expirationDate: String = response.get("expiration_date").asString
                genericResponse = LoginSuccessData(accessToken,statusJson,expirationDate,0,message,partnerId,"","",tokenType)
                emit(DataState.Success(genericResponse))
            }
            if (statusJson == 401) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                apiErrorModel = ErrorDataModel(statusJson, message,status)
                emit(DataState.ServerError(apiErrorModel))
            }

        } catch (e: Exception) {
            Log.i("Ex",e.toString())
            emit(DataState.Error(e))
        }
    }
}