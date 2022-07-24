package com.codex.ventorapp.main.inventory_adjustment.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.inventory_adjustment.intent.InventoryAdjustmentIntent
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.model.BarCodeList
import com.codex.ventorapp.main.inventory_adjustment.repository.AdjustmentRepo
import com.codex.ventorapp.main.model.DataModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class InventoryAdjustmentViewModel constructor(
    private val adjustmentRepo: AdjustmentRepo
) : ViewModel() {
    val userIntent = Channel<InventoryAdjustmentIntent>(Channel.UNLIMITED)

    private val adjustListUpdate: MutableLiveData<DataState<AdjustList>> = MutableLiveData()
    val adjustListRead: LiveData<DataState<AdjustList>>
        get() = adjustListUpdate

    private val barCodeUpdate: MutableLiveData<DataState<BarCodeList>> = MutableLiveData()
    val barCodeRead: LiveData<DataState<BarCodeList>>
        get() = barCodeUpdate

    private val inventoryAdjustmentUpdate: MutableLiveData<DataState<DataModel>> = MutableLiveData()
    val inventoryAdjustmentRead: LiveData<DataState<DataModel>>
        get() = inventoryAdjustmentUpdate

    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is InventoryAdjustmentIntent.GetInventoryAdjustment -> getInventoryAdjustment(it.token)
                    is InventoryAdjustmentIntent.GetBarCodeProduct -> getBarCodeProducts(
                        it.token,
                        it.id,
                        it.locationId
                    )
                    is InventoryAdjustmentIntent.GetUpdateInventory -> getInventoryAdjustment(it.token,it.product_id,it.location_id,it.stock_quant_id,it.inventory_quantity)
                }
            }
        }
    }

    private fun getInventoryAdjustment(token: String, productId: String, locationId: String, stockQuantityId: String, inventoryQuantity: String) {
        viewModelScope.launch {
            adjustmentRepo.getUpdateInventory(token, productId,locationId,stockQuantityId,inventoryQuantity)
                .onEach { dataState ->
                    inventoryAdjustmentUpdate.value = dataState
                }.launchIn(viewModelScope)
        }

    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getInventoryAdjustment(token: String) {
        viewModelScope.launch {
            adjustmentRepo.getAdjustmentList(token)
                .onEach { dataState ->
                    adjustListUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    private fun getBarCodeProducts(token: String, id: String, locationId: String) {
        viewModelScope.launch {
            adjustmentRepo.getBarCodeProduct(token, id,locationId)
                .onEach { dataState ->
                    barCodeUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }

}