package com.codex.ventorapp.main.accessToken.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.accessToken.intent.AccessTokenIntents
import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.accessToken.repository.AccessTokenRepos

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class AccessTokenViewModels constructor(
    private val accessTokenRepo: AccessTokenRepos
) : ViewModel() {
     val userIntent = Channel<AccessTokenIntents>(Channel.UNLIMITED)

    private val accessTokenUpdate: MutableLiveData<DataState<AccessTokenModel>> = MutableLiveData()
    val accessTokenRead: LiveData<DataState<AccessTokenModel>>
        get() = accessTokenUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is AccessTokenIntents.GetAccessToken -> getAccessToken(
                        it.client_id,
                        it.client_secret,
                        it.grant_type
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getAccessToken(client_id: String, client_secret: String, grant_type: String) {
        viewModelScope.launch {
            accessTokenRepo.getAccessToken(client_id, client_secret, grant_type)
                .onEach { dataState ->
                    accessTokenUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}