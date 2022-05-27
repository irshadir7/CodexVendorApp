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
    fun getLogin(login: String, password: String): Flow<DataState<LoginSuccessData>> = flow {
        try {
            val apiErrorModel: ErrorDataModel
            val response: JsonObject = loginAPI.getLogin(login,password)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: LoginSuccessData?
            if (statusJson == 200) {
                val accessToken: String = response.get("access_token").asString
                val message: String = response.get("message").asString
                val tokenType: String = response.get("token_type").asString
                genericResponse = LoginSuccessData(accessToken,statusJson,0,message,"","",tokenType)
                emit(DataState.Success(genericResponse))
            }
            if (statusJson == 401) {
                apiErrorModel = ErrorDataModel(statusJson, "","")
                emit(DataState.ServerError(apiErrorModel))
            }

        } catch (e: Exception) {
            Log.i("Ex",e.toString())
            emit(DataState.Error(e))
        }
    }
}