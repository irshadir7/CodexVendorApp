package com.codex.ventorapp.main.location.repository

import android.util.Log
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.location.model.LocationList
import com.codex.ventorapp.main.location.model.Locations
import com.codex.ventorapp.main.location.network.LocationApi
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LocationRepo
@Inject
constructor(private val locationApi: LocationApi) {
    fun getLocationList(token: String): Flow<DataState<LocationList>> = flow {
        emit(DataState.Loading)
        try {
            val bearerToken = "Bearer $token"
            val gson = Gson()
            val response: JsonObject = locationApi.getLocationList(bearerToken)
            val statusJson: Int = response.get("code").asInt
            val genericResponse: LocationList?
            if (statusJson == 200) {
                val message: String = response.get("message").asString
                val status: String = response.get("status").asString
                val dataJson: JsonArray = response.get("result").asJsonArray
                val packagesArray =
                    gson.fromJson(dataJson, Array<Locations>::class.java).toList()
                genericResponse = LocationList(statusJson, message, packagesArray, status)
                emit(DataState.Success(genericResponse))
            }

        } catch (e: Exception) {
            Log.i("Ex", e.toString())
            emit(DataState.Error(e))
        }
    }


}