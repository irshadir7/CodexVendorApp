package com.demo.end.main.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.end.main.intent.MainScreenIntent
import com.demo.end.main.model.EndProductsModel
import com.demo.end.main.repository.MainRepo
import com.demo.end.foundatiion.utilz.DataState
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@ExperimentalCoroutinesApi
@FlowPreview
@InternalCoroutinesApi
class ProductsListViewModel
constructor(
    private val mainRepo: MainRepo
): ViewModel() {
    
    val userIntent = Channel<MainScreenIntent>(Channel.UNLIMITED)

    private val generalProductsUpdate: MutableLiveData<DataState<EndProductsModel>> = MutableLiveData()
    val generalProductsRead: LiveData<DataState<EndProductsModel>>
        get() = generalProductsUpdate

    private val sneakersProductsUpdate: MutableLiveData<DataState<EndProductsModel>> = MutableLiveData()
    val sneakerProductsRead: LiveData<DataState<EndProductsModel>>
        get() = sneakersProductsUpdate


    init {
        handleIntent()
    }
    private fun handleIntent() {
        viewModelScope.launch(Dispatchers.IO) {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is MainScreenIntent.GetProducts -> getProducts()
                    is MainScreenIntent.GetMoreProducts -> getMoreProducts()
                }
            }
        }
    }

    private fun getProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.getGeneralProducts()
                .onEach { dataState ->
                    generalProductsUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }

    private fun getMoreProducts() {
        viewModelScope.launch(Dispatchers.IO) {
            mainRepo.getSneakersProducts()
                .onEach { dataState ->
                    sneakersProductsUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }

}