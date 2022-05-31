package com.codex.ventorapp.main.signup.vm
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.main.login.intent.LoginIntent
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.repository.LoginRepo
import com.codex.ventorapp.main.signup.intent.SignUpIntent
import com.codex.ventorapp.main.signup.model.SignupSuccessModel
import com.codex.ventorapp.main.signup.repository.SignupRepo
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class SignUpViewModel constructor(
    private val signUpRepo: SignupRepo
) : ViewModel() {
    val userIntent = Channel<SignUpIntent>(Channel.UNLIMITED)

    private val signUpDataUpdate: MutableLiveData<DataState<SignupSuccessModel>> = MutableLiveData()
    val signUpDataRead: LiveData<DataState<SignupSuccessModel>>
        get() = signUpDataUpdate


    init {
        handleIntent()
    }

    // The ViewModel handles these events and communicates with the Model.
    private fun handleIntent() {
        viewModelScope.launch {
            userIntent.consumeAsFlow().collect {
                when (it) {
                    is SignUpIntent.GetSignUP -> getSignUp(
                        it.token,
                        it.email,
                        it.name,
                        it.password
                    )
                }
            }
        }
    }

    //As a result, the ViewModel updates the View with new states and is then displayed to the user.
    private fun getSignUp(token: String,email: String, name: String,password: String) {
        viewModelScope.launch {
            signUpRepo.getSignUp(token,email,name, password)
                .onEach { dataState ->
                    signUpDataUpdate.value = dataState
                }.launchIn(viewModelScope)
        }
    }
}