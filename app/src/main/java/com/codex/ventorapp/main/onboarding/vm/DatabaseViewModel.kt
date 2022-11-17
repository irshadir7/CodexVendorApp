package com.codex.ventorapp.main.onboarding.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.login.intent.LoginIntent
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.repository.LoginRepo
import com.codex.ventorapp.main.onboarding.intent.DatabaseIntent
import com.codex.ventorapp.main.onboarding.model.DatabaseList
import com.codex.ventorapp.main.onboarding.repo.DatabaseRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class DatabaseViewModel constructor(
    private val databaseRepo: DatabaseRepo
) : ViewModel() {
    val userIntent = Channel<DatabaseIntent>(Channel.UNLIMITED)

    private val databaseUpdate: MutableLiveData<DataState<DatabaseList>> = MutableLiveData()
    val databaseRead: LiveData<DataState<DatabaseList>>
        get() = databaseUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is DatabaseIntent.GetDataBase -> getDatabase(
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getDatabase() {
        viewModelScope.launch {
            databaseRepo.getDatabase()
                .onEach { dataState ->
                    databaseUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}