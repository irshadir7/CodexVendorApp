package com.codex.ventorapp.main.receipt.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.receipt.intent.ReceiptIntent
import com.codex.ventorapp.main.receipt.model.ListPicking
import com.codex.ventorapp.main.receipt.repository.ReceiptRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReceiptViewModel constructor(
    private val receiptRepo: ReceiptRepo
) : ViewModel() {
    val userIntent = Channel<ReceiptIntent>(Channel.UNLIMITED)

    private val receiptDataUpdate: MutableLiveData<DataState<ListPicking>> = MutableLiveData()
    val receiptDataRead: LiveData<DataState<ListPicking>>
        get() = receiptDataUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ReceiptIntent.GetReceipt -> getReceipt(
                        it.token,
                        it.partner_id
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getReceipt(token: String,partner_id: String) {
        viewModelScope.launch {
            receiptRepo.getReceiptList(token,partner_id)
                .onEach { dataState ->
                    receiptDataUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}