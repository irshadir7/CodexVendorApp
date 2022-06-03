package com.codex.ventorapp.main.inventory_adjustment.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.inventory_adjustment.intent.InventoryAdjustmentIntent
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.repository.AdjustmentRepo
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


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is InventoryAdjustmentIntent.GetInventoryAdjustment -> getInventoryAdjustment(it.token)
                }
            }
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
}