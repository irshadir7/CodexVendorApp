package com.codex.ventorapp.main.purchase.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.model.BarCodeList
import com.codex.ventorapp.main.inventory_adjustment.model.ResultAdjustment
import com.codex.ventorapp.main.inventory_adjustment.model.ResultBarCode
import com.codex.ventorapp.main.inventory_adjustment.network.InventoryAdjustmentApi
import com.codex.ventorapp.main.model.DataModel
import com.codex.ventorapp.main.model.ErrorDataModel
import com.codex.ventorapp.main.purchase.model.Customer
import com.codex.ventorapp.main.purchase.model.CustomerList
import com.codex.ventorapp.main.purchase.model.PurchaseOrder
import com.codex.ventorapp.main.purchase.model.ResultPurchase
import com.codex.ventorapp.main.purchase.network.PurchaseApi

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class PurchaseRepo
@Inject
constructor(private val purchaseApi: PurchaseApi) {
    fun getPurchaseList(token: String): Flow<DataState<PurchaseOrder>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            val gson = Gson()
            val response: JsonObject = purchaseApi.getPurchaseList(bearerToken)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: PurchaseOrder?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                val dataJson: JsonArray = response.get("result").asJsonArray
                val packagesArray =
                    gson.fromJson(dataJson, Array<ResultPurchase>::class.java).toList()
                genericResponse = PurchaseOrder(statusJson, message, packagesArray, status)
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


    fun getCustomerList(token: String): Flow<DataState<Customer>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            val gson = Gson()
            val response: JsonObject = purchaseApi.getAllCustomer(bearerToken)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: Customer?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                val dataJson: JsonArray = response.get("result").asJsonArray
                val packagesArray =
                    gson.fromJson(dataJson, Array<CustomerList>::class.java).toList()
                genericResponse = Customer(statusJson, message, packagesArray, status)
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


}