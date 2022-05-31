package com.codex.ventorapp.main.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.codex.ventorapp.R

import com.codex.ventorapp.foundatiion.mvi.BaseActivity
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.foundatiion.utilz.Utils
import com.codex.ventorapp.main.accessToken.intent.AccessTokenIntents
import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.accessToken.vm.AccessTokenViewModels
import com.codex.ventorapp.main.adapters.PhotoAdapter
import com.codex.ventorapp.main.login.intent.LoginIntent
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.vm.LoginViewModel
import com.codex.ventorapp.main.model.DataModel
import com.codex.ventorapp.main.signup.intent.SignUpIntent
import com.codex.ventorapp.main.signup.model.SignupSuccessModel
import com.codex.ventorapp.main.signup.vm.SignUpViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class MainActivity : BaseActivity() {
    @Inject
    lateinit var accessTokenViewModel: AccessTokenViewModels

    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var signUpViewModel: SignUpViewModel
    private var dataList = mutableListOf<DataModel>()
    private lateinit var photoAdapter: PhotoAdapter
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        session = SessionManager(this)
        init()
    }

    private fun init() {
        photoAdapter = PhotoAdapter(applicationContext)

        recyclerview.setHasFixedSize(true)
        recyclerview.apply {
            layoutManager = GridLayoutManager(
                context,
                4
            )
            adapter = photoAdapter
        }
        prepareItems()
        authRefresh()
        authLogin()
    }

    private fun prepareItems() {
        dataList.add(DataModel(1, "Warehouse Operation", R.drawable.page_wh__state_default))
        dataList.add(DataModel(2, "Receipts", R.drawable.page_about__state_default))
        dataList.add(DataModel(3, "Delivery Orders", R.drawable.page_about__state_default))
        dataList.add(DataModel(4, "Returns", R.drawable.page_about__state_default))
        dataList.add(DataModel(5, "POS Orders", R.drawable.page_about__state_default))
        dataList.add(DataModel(6, "Batch picking", R.drawable.page_batch__state_default))
        dataList.add(DataModel(7, "Internal transfers", R.drawable.page_internal__state_default))
        dataList.add(DataModel(8, "Quick info", R.drawable.page_quick__state_default))
        dataList.add(DataModel(9, "Putaway", R.drawable.page_putaway))
        dataList.add(DataModel(10, "Instant inventory", R.drawable.page_ii__state_default))
        dataList.add(
            DataModel(
                11,
                "Inventory adjustment",
                R.drawable.page_inventory__state_default
            )
        )
        dataList.add(DataModel(12, "External transfers", R.drawable.page_external__state_default))
        dataList.add(DataModel(13, "MO and WO management", R.drawable.page_manufacturing))
        dataList.add(DataModel(14, "Create SO", R.drawable.page_sales__state_default))
        dataList.add(DataModel(15, "Create PO", R.drawable.page_purchase__state_default))
        dataList.add(DataModel(16, "Leave feedback", R.drawable.page_feedback__state_default))
        dataList.add(DataModel(17, "Settings", R.drawable.page_setting__state_default))
        dataList.add(DataModel(18, "About", R.drawable.page_about__state_default))
        dataList.add(DataModel(19, "Logout", R.drawable.page_logout__state_default))
        photoAdapter.setDataList(dataList)
    }


    private fun authRefresh() {
        lifecycleScope.launch {
            accessTokenViewModel.userIntent.send(
                AccessTokenIntents.GetAccessToken(
                    Utils.CLIENT_ID,
                    Utils.CLIENT_SECRET,
                    Utils.GRANT_TYPE
                )
            )
        }

        subscribeObservers()
    }

    private fun authLogin() {
        lifecycleScope.launch {
            loginViewModel.userIntent.send(
                LoginIntent.GetLogin(
                    "ashifpk1@gmail.com",
                    "123"
                )
            )
        }

        subscribeObserversForLogin()
    }


    private fun authSignUp(token: String) {
        lifecycleScope.launch {
            signUpViewModel.userIntent.send(
                SignUpIntent.GetSignUP(
                    token,
                    "mpirshu06@gmail.com",
                    "Irshad",
                    "Irshad123"
                )
            )
        }

        subscribeObserversForSignUp()
    }

    private fun subscribeObservers() {
        accessTokenViewModel.accessTokenRead.observe(
            this
        ) { accessTokenRead ->
            when (accessTokenRead) {
                is DataState.Success<AccessTokenModel> -> {
                    Log.d("AccessToken", accessTokenRead.data.toString())
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                    authSignUp(accessTokenRead.data.access_token)
                }
                is DataState.Error -> {
                    Log.d("AccessToken", accessTokenRead.exception.toString())
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                }
            }
        }
    }


    private fun subscribeObserversForLogin() {
        loginViewModel.loginDataRead.observe(
            this
        ) { loginDataRead ->
            when (loginDataRead) {
                is DataState.Success<LoginSuccessData> -> {
                    Log.d("LoginFlow", loginDataRead.data.toString())
                    loginViewModel.loginDataRead.removeObservers(this)
                    session.createLoginSession(loginDataRead.data)
                }
                is DataState.ServerError -> {
                    Log.d("LoginFlow", loginDataRead.response.toString())
                    loginViewModel.loginDataRead.removeObservers(this)
                }
                is DataState.Error -> {
                    Log.d("LoginFlow", loginDataRead.exception.toString())
                    loginViewModel.loginDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    accessTokenViewModel.accessTokenRead.removeObservers(this)
                }
            }
        }
    }

    private fun subscribeObserversForSignUp() {
        signUpViewModel.signUpDataRead.observe(
            this
        ) { signUpDataRead ->
            when (signUpDataRead) {
                is DataState.Success<SignupSuccessModel> -> {
                    Log.d("SignUpFlow", signUpDataRead.data.toString())
                    signUpViewModel.signUpDataRead.removeObservers(this)

                }
                is DataState.ServerError -> {
                    Log.d("SignUpFlow", signUpDataRead.response.toString())
                    signUpViewModel.signUpDataRead.removeObservers(this)
                }
                is DataState.Error -> {
                    Log.d("SignUpFlow", signUpDataRead.exception.toString())
                    signUpViewModel.signUpDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    signUpViewModel.signUpDataRead.removeObservers(this)
                }
            }
        }
    }
}