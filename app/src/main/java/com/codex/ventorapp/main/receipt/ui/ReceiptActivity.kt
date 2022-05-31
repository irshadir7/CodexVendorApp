package com.codex.ventorapp.main.receipt.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.receipt.adapter.ReceiptListAdapter
import com.codex.ventorapp.main.receipt.intent.ReceiptIntent
import com.codex.ventorapp.main.receipt.model.ListPicking
import com.codex.ventorapp.main.receipt.vm.ReceiptViewModel
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
class ReceiptActivity : AppCompatActivity() {
    @Inject
    lateinit var receiptViewModel: ReceiptViewModel
    private lateinit var receiptListAdapter: ReceiptListAdapter
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recipt)
        supportActionBar?.hide()
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        val partnerId: String = user[SessionManager.PARTNER_ID]!!
        val tokenValue: String = user[SessionManager.USER_TOKEN]!!
        authReceiptList(partnerId,tokenValue)
        init()
    }

    private fun init() {
        receiptListAdapter = ReceiptListAdapter(applicationContext)

        rvReceiptList.setHasFixedSize(true)
        rvReceiptList.apply {
            layoutManager = LinearLayoutManager(
                context,
            )
            adapter = receiptListAdapter
        }

    }
//Todo
    private fun authReceiptList(partnerId: String, tokenValue: String) {
        lifecycleScope.launch {
            receiptViewModel.userIntent.send(
                ReceiptIntent.GetReceipt(
                    tokenValue,
                    "7",
                )
            )
        }

        subscribeObservers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeObservers() {
        receiptViewModel.receiptDataRead.observe(
            this
        ) { receiptDataRead ->
            when (receiptDataRead) {
                is DataState.Success<ListPicking> -> {
                    Log.d("ReceiptList", receiptDataRead.data.toString())
                    receiptViewModel.receiptDataRead.removeObservers(this)
                    receiptListAdapter.setDataList(receiptDataRead.data.result)
                    receiptListAdapter.notifyDataSetChanged()
                }
                is DataState.Error -> {
                    Log.d("ReceiptList", receiptDataRead.exception.toString())
                    receiptViewModel.receiptDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {

                    receiptViewModel.receiptDataRead.removeObservers(this)
                }
            }
        }
    }
}