package com.codex.ventorapp.main.inventory_adjustment.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.model.ResultAdjustment
import com.codex.ventorapp.main.inventory_adjustment.network.InventoryAdjustmentApi
import com.codex.ventorapp.main.receipt.model.ListPicking
import com.codex.ventorapp.main.receipt.model.Results
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AdjustmentRepo
@Inject
constructor(private val inventoryAdjustmentApi: InventoryAdjustmentApi) {
    fun getAdjustmentList(token: String): Flow<DataState<AdjustList>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            val gson = Gson()
            val response: JsonObject = inventoryAdjustmentApi.getAdjustList(bearerToken)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: AdjustList?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                val dataJson: JsonArray = response.get("result").asJsonArray
                val packagesArray =
                    gson.fromJson(dataJson, Array<ResultAdjustment>::class.java).toList()
                genericResponse = AdjustList(statusJson, message, packagesArray, status)
                emit(DataState.Success(genericResponse))
            }

        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }
}