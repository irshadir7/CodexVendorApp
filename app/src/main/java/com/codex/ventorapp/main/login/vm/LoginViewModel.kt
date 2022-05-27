package com.codex.ventorapp.main.login.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.login.intent.LoginIntent
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.repository.LoginRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class LoginViewModel constructor(
    private val loginRepo: LoginRepo
) : ViewModel() {
    val userIntent = Channel<LoginIntent>(Channel.UNLIMITED)

    private val loginDataUpdate: MutableLiveData<DataState<LoginSuccessData>> = MutableLiveData()
    val loginDataRead: LiveData<DataState<LoginSuccessData>>
        get() = loginDataUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is LoginIntent.GetLogin -> getLogin(
                        it.login,
                        it.password
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getLogin(login: String, password: String) {
        viewModelScope.launch {
            loginRepo.getLogin(login, password)
                .onEach { dataState ->
                    loginDataUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}