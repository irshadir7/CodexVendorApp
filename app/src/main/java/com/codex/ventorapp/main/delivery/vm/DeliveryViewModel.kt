package com.codex.ventorapp.main.delivery.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.delivery.intent.DeliveryIntent
import com.codex.ventorapp.main.delivery.repository.DeliveryRepo
import com.codex.ventorapp.main.receipt.intent.ReceiptIntent
import com.codex.ventorapp.main.receipt.model.ListPicking
import com.codex.ventorapp.main.receipt.repository.ReceiptRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DeliveryViewModel constructor(
    private val deliveryRepo: DeliveryRepo
) : ViewModel() {
     val userIntent = Channel<DeliveryIntent>(Channel.UNLIMITED)

    private val deliveryDataUpdate: MutableLiveData<DataState<ListPicking>> = MutableLiveData()
    val deliveryDataRead: LiveData<DataState<ListPicking>>
        get() = deliveryDataUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is DeliveryIntent.GetDelivery -> getDelivery(
                        it.token,
                        it.partner_id
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getDelivery(token: String,partner_id: String) {
        viewModelScope.launch {
            deliveryRepo.getDeliveryList(token,partner_id)
                .onEach { dataState ->
                    deliveryDataUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}