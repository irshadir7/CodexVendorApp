package com.codex.ventorapp.main.delivery.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.delivery.network.DeliveryApi
import com.codex.ventorapp.main.receipt.model.ListPicking
import com.codex.ventorapp.main.receipt.model.Results
import com.codex.ventorapp.main.receipt.network.ReceiptApi
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class DeliveryRepo
@Inject
constructor(private val deliveryAPI: DeliveryApi) {
    fun getDeliveryList(token: String, partner_id: String): Flow<DataState<ListPicking>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            val gson = Gson()
            val response: JsonObject = deliveryAPI.getDelivery(bearerToken, partner_id)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: ListPicking?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                val dataJson: JsonArray = response.get("result").asJsonArray
                val packagesArray = gson.fromJson(dataJson, Array<Results>::class.java).toList()
                genericResponse = ListPicking(statusJson, message, packagesArray, status)
                emit(DataState.Success(genericResponse))
            }

        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }
}