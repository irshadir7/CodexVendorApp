package com.codex.ventorapp.main.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.login.intent.LoginIntent
import com.codex.ventorapp.main.login.model.LoginSuccessData
import com.codex.ventorapp.main.login.vm.LoginViewModel
import com.codex.ventorapp.main.onboarding.intent.DatabaseIntent
import com.codex.ventorapp.main.onboarding.model.DatabaseList
import com.codex.ventorapp.main.onboarding.vm.DatabaseViewModel
import com.codex.ventorapp.main.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_database_choose.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.util.ArrayList
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class DatabaseChooseActivity : AppCompatActivity() {
    @Inject
    lateinit var loginViewModel: LoginViewModel

    @Inject
    lateinit var databaseViewModel: DatabaseViewModel
    private lateinit var session: SessionManager
    private lateinit var dataBase: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_database_choose)
        session = SessionManager(this)
        authDatabase()

    }

    private fun authLogin(dataBase: String) {
        Log.d("dataBase", dataBase)
        lifecycleScope.launch {
            loginViewModel.userIntent.send(
                LoginIntent.GetLogin(
                    session.getDataBase(),
                    tvUserName.text.toString(),
                    tvPassword.text.toString()
                )
            )
        }

        subscribeObserversForLogin()
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
                    val intent = Intent(this@DatabaseChooseActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                is DataState.ServerError -> {
                    Log.d("LoginFlow", loginDataRead.response.toString())
                    val toast = Toast.makeText(
                        applicationContext,
                        loginDataRead.response.message,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    loginViewModel.loginDataRead.removeObservers(this)
                }
                is DataState.Error -> {
                    val toast = Toast.makeText(
                        applicationContext,
                        loginDataRead.exception.toString(),
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    Log.d("LoginFlow", loginDataRead.exception.toString())
                    loginViewModel.loginDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    loginViewModel.loginDataRead.removeObservers(this)
                }
            }
        }
    }


    private fun authDatabase() {
        lifecycleScope.launch {
            databaseViewModel.userIntent.send(
                DatabaseIntent.GetDataBase
            )
        }

        subscribeObserversForDataBase()
    }

    private fun subscribeObserversForDataBase() {
        databaseViewModel.databaseRead.observe(
            this
        ) { databaseRead ->
            when (databaseRead) {
                is DataState.Success<DatabaseList> -> {
                    val list = ArrayList<String>()
                    val databaseList: List<String> = databaseRead.data.result.toString().split(",")
                        .map { it.replace("[", "").replace("]", "").trim() }
                    databaseList.forEach {
                        list.add(it)
                    }
                    addToSpinner(list)
                    databaseViewModel.databaseRead.removeObservers(this)
                    Log.d("databaseRead", databaseList.toString())
                }
                is DataState.ServerError -> {
                    Log.d("databaseRead", databaseRead.response.toString())
                    val toast = Toast.makeText(
                        applicationContext,
                        databaseRead.response.message,
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    databaseViewModel.databaseRead.removeObservers(this)
                }
                is DataState.Error -> {
                    val toast = Toast.makeText(
                        applicationContext,
                        databaseRead.exception.toString(),
                        Toast.LENGTH_SHORT
                    )
                    toast.show()
                    databaseViewModel.databaseRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    databaseViewModel.databaseRead.removeObservers(this)
                }
            }
        }
    }


    private fun addToSpinner(list: ArrayList<String>) {
        val databaseAdapter = ArrayAdapter(
            this, // Context
            R.layout.spinner_item,
            list
        )
        spinnerDatabase.adapter = databaseAdapter

        spinnerDatabase?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                dataBase = list[position]
                session.saveDb(list[position])
            }

        }
        loginBtn.setOnClickListener {
            if (tvUserName.text.toString().isNotEmpty() && tvPassword.text.toString()
                    .isNotEmpty()
            ) {
                authLogin(dataBase)
            } else {
                val toast = Toast.makeText(
                    applicationContext,
                    "Please fill required values",
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }

        }

    }
}