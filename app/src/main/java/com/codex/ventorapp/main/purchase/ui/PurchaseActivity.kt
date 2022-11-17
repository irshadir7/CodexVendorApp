package com.codex.ventorapp.main.purchase.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.mvi.BaseActivity
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.purchase.adapter.PurchaseOrderTableAdapter
import com.codex.ventorapp.main.purchase.intent.PurchaseOrderIntent
import com.codex.ventorapp.main.purchase.model.PurchaseOrder
import com.codex.ventorapp.main.purchase.vm.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_purchase.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class PurchaseActivity : BaseActivity() {
    @Inject
    lateinit var purchaseViewModel: PurchaseViewModel
    private lateinit var session: SessionManager
    private var tokenValue = ""
    private lateinit var purchaseOrderTableAdapter: PurchaseOrderTableAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase)
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        tokenValue = user[SessionManager.USER_TOKEN]!!
        authList(tokenValue)
        init()
        createBtn.setOnClickListener{
            val intent = Intent(this, CreatePurchaseOrderActivity::class.java)
            startActivity(intent)
        }
    }

    private fun authList(tokenValue: String) {
        lifecycleScope.launch {
            purchaseViewModel.userIntent.send(
                PurchaseOrderIntent.GetPurchaseOrder(
                    tokenValue
                )
            )
        }

        subscribeObservers()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeObservers() {
        purchaseViewModel.purchaseListRead.observe(
            this
        ) { purchaseListRead ->
            when (purchaseListRead) {
                is DataState.Success<PurchaseOrder> -> {
                    Log.d("PurchaseOrder", purchaseListRead.data.toString())
                    purchaseViewModel.purchaseListRead.removeObservers(this)
                    purchaseOrderTableAdapter.setDataList(purchaseListRead.data.result)
                    purchaseOrderTableAdapter.notifyDataSetChanged()
                    stopLoading()
                }
                is DataState.Error -> {
                    Log.d("PurchaseOrder", purchaseListRead.exception.toString())
                    purchaseViewModel.purchaseListRead.removeObservers(this)
                    stopLoading()
                }
                is DataState.ServerError -> {
                    purchaseViewModel.purchaseListRead.removeObservers(this)
                    stopLoading()
                }
                is DataState.Loading -> {
                    loading()
                }
                else -> {
                    stopLoading()
                    purchaseViewModel.purchaseListRead.removeObservers(this)
                }
            }
        }
    }

    private fun init() {
        purchaseOrderTableAdapter = PurchaseOrderTableAdapter(applicationContext)
        rvPurchaseOrder
            .setHasFixedSize(true)
        rvPurchaseOrder.apply {
            layoutManager = LinearLayoutManager(
                context,
            )
            adapter = purchaseOrderTableAdapter
        }

    }
}