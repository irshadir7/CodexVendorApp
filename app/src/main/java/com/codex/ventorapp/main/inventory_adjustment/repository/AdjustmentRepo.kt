package com.codex.ventorapp.main.inventory_adjustment.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.model.BarCodeList
import com.codex.ventorapp.main.inventory_adjustment.model.ResultAdjustment
import com.codex.ventorapp.main.inventory_adjustment.model.ResultBarCode
import com.codex.ventorapp.main.inventory_adjustment.network.InventoryAdjustmentApi
import com.codex.ventorapp.main.model.DataModel
import com.codex.ventorapp.main.model.ErrorDataModel

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
            } else {
                val apiErrorModel = ErrorDataModel(statusJson, "message", "")
                emit(DataState.ServerError(apiErrorModel))
            }

        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }


    fun getBarCodeProduct(
        token: String,
        id: String,
        locationId: String
    ): Flow<DataState<BarCodeList>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            val gson = Gson()
            val response: JsonObject =
                inventoryAdjustmentApi.getBarCodeProduct(id, bearerToken, locationId)
            Log.d("LocationID", locationId)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: BarCodeList?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                val dataJson: JsonObject = response.get("result").asJsonObject
                val packagesArray = gson.fromJson(dataJson, ResultBarCode::class.java)
                genericResponse = BarCodeList(statusJson, message, packagesArray, status)
                emit(DataState.Success(genericResponse))
            } else if (statusJson == 400) {
                val apiErrorModel: ErrorDataModel
                val message: String = response.get("message").asString
                apiErrorModel = ErrorDataModel(statusJson, message, "")
                emit(DataState.ServerError(apiErrorModel))
            }

        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }


    fun getUpdateInventory(
        token: String,
        productId: String,
        locationId: String,
        stockQuantityId: String,
        inventoryQuantity: String
    ): Flow<DataState<DataModel>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            Log.d("InventoryUpdate", bearerToken)
            Log.d("InventoryUpdate", productId)
            Log.d("InventoryUpdate", locationId)
            Log.d("InventoryUpdate", inventoryQuantity)
            val response: JsonObject = inventoryAdjustmentApi.getInventoryAdjustment(
                bearerToken,
                productId,
                locationId,
                stockQuantityId,
                inventoryQuantity
            )
            Log.d("InventoryUpdate", response.toString())
            val statusJson: Int = response.get("code").asInt
            val genericResponse: DataModel?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                genericResponse = DataModel(statusJson, message, status.toInt())
                emit(DataState.Success(genericResponse))
            } else {
                val apiErrorModel: ErrorDataModel
                val message: String = response.get("message").asString
                apiErrorModel = ErrorDataModel(statusJson, message, "")
                emit(DataState.ServerError(apiErrorModel))
            }

        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }
}