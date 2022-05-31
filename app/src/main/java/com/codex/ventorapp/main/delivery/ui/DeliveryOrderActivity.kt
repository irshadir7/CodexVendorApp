package com.codex.ventorapp.main.delivery.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.delivery.intent.DeliveryIntent
import com.codex.ventorapp.main.delivery.vm.DeliveryViewModel
import com.codex.ventorapp.main.receipt.adapter.ReceiptListAdapter
import com.codex.ventorapp.main.receipt.model.ListPicking
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_recipt.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class DeliveryOrderActivity : AppCompatActivity() {
    @Inject
    lateinit var deliveryViewModel: DeliveryViewModel
    private lateinit var session: SessionManager
    private lateinit var deliveryListAdapter: ReceiptListAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_delivery_order)
        supportActionBar?.hide()
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        val partnerId: String = user[SessionManager.PARTNER_ID]!!
        val tokenValue: String = user[SessionManager.USER_TOKEN]!!
        authDeliveryList(partnerId, tokenValue)
        init()
    }

    private fun init() {
        deliveryListAdapter = ReceiptListAdapter(applicationContext)

        rvReceiptList.setHasFixedSize(true)
        rvReceiptList.apply {
            layoutManager = LinearLayoutManager(
                context,
            )
            adapter = deliveryListAdapter
        }

    }

    private fun authDeliveryList(partnerId: String, tokenValue: String) {
        lifecycleScope.launch {
            deliveryViewModel.userIntent.send(
                DeliveryIntent.GetDelivery(
                    tokenValue,
                    "7",
                )
            )
        }

        subscribeObserversDelivery()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeObserversDelivery() {
        deliveryViewModel.deliveryDataRead.observe(
            this
        ) { deliveryDataRead ->
            when (deliveryDataRead) {
                is DataState.Success<ListPicking> -> {
                    Log.d("DeliveryList", deliveryDataRead.data.toString())
                    deliveryViewModel.deliveryDataRead.removeObservers(this)
                    deliveryListAdapter.setDataList(deliveryDataRead.data.result)
                    deliveryListAdapter.notifyDataSetChanged()
                }
                is DataState.Error -> {
                    Log.d("DeliveryList", deliveryDataRead.exception.toString())
                    deliveryViewModel.deliveryDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    deliveryViewModel.deliveryDataRead.removeObservers(this)
                }
            }
        }
    }
}