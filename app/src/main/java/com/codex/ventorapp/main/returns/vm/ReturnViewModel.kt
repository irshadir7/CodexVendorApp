package com.codex.ventorapp.main.returns.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.receipt.intent.ReceiptIntent
import com.codex.ventorapp.main.receipt.model.ListPicking
import com.codex.ventorapp.main.receipt.repository.ReceiptRepo
import com.codex.ventorapp.main.returns.intent.ReturnIntent
import com.codex.ventorapp.main.returns.repository.ReturnRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class ReturnViewModel constructor(
    private val returnRepo: ReturnRepo
) : ViewModel() {
    val userIntent = Channel<ReturnIntent>(Channel.UNLIMITED)

    private val returnDataUpdate: MutableLiveData<DataState<ListPicking>> = MutableLiveData()
    val returnDataRead: LiveData<DataState<ListPicking>>
        get() = returnDataUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is ReturnIntent.GetReturn -> getReturn(
                        it.token,
                        it.partner_id
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getReturn(token: String,partner_id: String) {
        viewModelScope.launch {
            returnRepo.getReturnList(token,partner_id)
                .onEach { dataState ->
                    returnDataUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}