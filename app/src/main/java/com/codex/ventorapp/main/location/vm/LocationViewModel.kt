package com.codex.ventorapp.main.location.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.location.intent.LocationIntent
import com.codex.ventorapp.main.location.model.LocationList
import com.codex.ventorapp.main.location.repository.LocationRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LocationViewModel constructor(
    private val locationRepo: LocationRepo
) : ViewModel() {
    val userIntent = Channel<LocationIntent>(Channel.UNLIMITED)

    private val locationDataUpdate: MutableLiveData<DataState<LocationList>> = MutableLiveData()
    val locationDataRead: LiveData<DataState<LocationList>>
        get() = locationDataUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is LocationIntent.GetLocation -> getLocation(
                        it.token)
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getLocation(token: String) {
        viewModelScope.launch {
            locationRepo.getLocationList(token)
                .onEach { dataState ->
                    locationDataUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}