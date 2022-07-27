package com.codex.ventorapp.main.purchase.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.lifecycle.lifecycleScope
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.mvi.BaseActivity
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.purchase.intent.PurchaseOrderIntent
import com.codex.ventorapp.main.purchase.model.Customer
import com.codex.ventorapp.main.purchase.vm.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_purchase_order.*
import kotlinx.android.synthetic.main.activity_inventory_adjustment.*
import kotlinx.android.synthetic.main.activity_inventory_adjustment.spinnerLocation
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class CreatePurchaseOrderActivity : BaseActivity() {
    @Inject
    lateinit var purchaseViewModel: PurchaseViewModel
    private lateinit var session: SessionManager
    private var tokenValue = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_purchase_order)
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        tokenValue = user[SessionManager.USER_TOKEN]!!
        authList(tokenValue)
    }

    private fun authList(tokenValue: String) {
        lifecycleScope.launch {
            purchaseViewModel.userIntent.send(
                PurchaseOrderIntent.GetCustomerList(
                    tokenValue
                )
            )
        }

        subscribeObservers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeObservers() {
        purchaseViewModel.customerListRead.observe(
            this
        ) { customerListRead ->
            when (customerListRead) {
                is DataState.Success<Customer> -> {
                    Log.d("ListCustomer", customerListRead.data.toString())
                    purchaseViewModel.customerListRead.removeObservers(this)
                    stopLoading()
                    addToSpinner(customerListRead.data)
                }
                is DataState.Error -> {
                    Log.d("ListCustomer", customerListRead.exception.toString())
                    purchaseViewModel.customerListRead.removeObservers(this)
                    stopLoading()
                }
                is DataState.ServerError -> {
                    purchaseViewModel.customerListRead.removeObservers(this)
                    stopLoading()
                }
                is DataState.Loading -> {
                    loading()
                }
                else -> {
                    stopLoading()
                    purchaseViewModel.customerListRead.removeObservers(this)
                }
            }
        }
    }

    private fun addToSpinner(data: Customer) {
        val list = ArrayList<String>()
        val listID = ArrayList<Int>()
        for (item in data.result) {
            list.add(item.name)
            listID.add(item.id)
        }
        val locationAdapter = ArrayAdapter(
            this, // Context
            android.R.layout.simple_spinner_item,
            list
        )
        spinnerUser.adapter = locationAdapter

        spinnerUser?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("ListCustomer", listID[position].toString())

            }

        }


    }
}