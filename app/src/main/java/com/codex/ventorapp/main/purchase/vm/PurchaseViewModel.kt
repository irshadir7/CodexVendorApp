package com.codex.ventorapp.main.purchase.vm

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
import com.codex.ventorapp.main.purchase.intent.PurchaseOrderIntent
import com.codex.ventorapp.main.purchase.model.PurchaseOrder
import com.codex.ventorapp.main.purchase.repository.PurchaseRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PurchaseViewModel constructor(
    private val purchaseRepo: PurchaseRepo
) : ViewModel() {
    val userIntent = Channel<PurchaseOrderIntent>(Channel.UNLIMITED)

    private val purchaseListUpdate: MutableLiveData<DataState<PurchaseOrder>> = MutableLiveData()
    val purchaseListRead: LiveData<DataState<PurchaseOrder>>
        get() = purchaseListUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is PurchaseOrderIntent.GetPurchaseOrder -> getPurchaseOrderList(it.token)
                }
            }
        }
    }

    private fun getPurchaseOrderList(token: String) {
        viewModelScope.launch {
            purchaseRepo.getPurchaseList(token)
                .onEach { dataState ->
                    purchaseListUpdate.value = dataState
                }.launchIn(viewModelScope)
        }

    }


}