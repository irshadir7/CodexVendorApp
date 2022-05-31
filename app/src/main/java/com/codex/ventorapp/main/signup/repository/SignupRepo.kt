package com.codex.ventorapp.main.signup.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.network.LoginApi
import com.codex.ventorapp.main.model.ErrorDataModel
import com.codex.ventorapp.main.signup.model.SignupSuccessModel
import com.codex.ventorapp.main.signup.network.SignupApi
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SignupRepo
@Inject
constructor(private val signupApi: SignupApi){
    fun getSignUp(token: String,email: String,name: String, password: String): Flow<DataState<SignupSuccessModel>> = flow {
        try {
            val tokenBearer= "Bearer $token"
            val apiErrorModel: ErrorDataModel
            val response: JsonObject = signupApi.getSignup(tokenBearer,email,name,password)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: SignupSuccessModel?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                genericResponse = SignupSuccessModel(statusJson,message,status)
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