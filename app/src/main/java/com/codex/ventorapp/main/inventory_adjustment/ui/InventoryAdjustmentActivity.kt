package com.codex.ventorapp.main.inventory_adjustment.ui

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.inventory_adjustment.adapter.InventoryListAdapter
import com.codex.ventorapp.main.inventory_adjustment.intent.InventoryAdjustmentIntent
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.vm.InventoryAdjustmentViewModel
import com.codex.ventorapp.main.receipt.adapter.ReceiptListAdapter
import com.codex.ventorapp.main.receipt.intent.ReceiptIntent
import com.codex.ventorapp.main.receipt.model.ListPicking
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_inventory_adjustment.*
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
class InventoryAdjustmentActivity : AppCompatActivity() {
    @Inject
    lateinit var inventoryListViewModel: InventoryAdjustmentViewModel
    private lateinit var inventoryListAdapter: InventoryListAdapter
    private lateinit var session: SessionManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_adjustment)
        supportActionBar?.hide()
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        val tokenValue: String = user[SessionManager.USER_TOKEN]!!
        authList(tokenValue)
        init()
    }
    private fun init() {
        inventoryListAdapter = InventoryListAdapter(applicationContext)
        rvInventoryList
        .setHasFixedSize(true)
        rvInventoryList.apply {
            layoutManager = LinearLayoutManager(
                context,
            )
            adapter = inventoryListAdapter
        }

    }


    private fun authList(tokenValue: String) {
        lifecycleScope.launch {
            inventoryListViewModel.userIntent.send(
                InventoryAdjustmentIntent.GetInventoryAdjustment(
                    tokenValue
                )
            )
        }

        subscribeObservers()
    }
    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeObservers() {
        inventoryListViewModel.adjustListRead.observe(
            this
        ) { adjustListRead ->
            when (adjustListRead) {
                is DataState.Success<AdjustList> -> {
                    Log.d("AdjustList", adjustListRead.data.toString())
                    inventoryListViewModel.adjustListRead.removeObservers(this)
                    inventoryListAdapter.setDataList(adjustListRead.data.result)
                    inventoryListAdapter.notifyDataSetChanged()
                }
                is DataState.Error -> {
                    Log.d("AdjustList", adjustListRead.exception.toString())
                    inventoryListViewModel.adjustListRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    inventoryListViewModel.adjustListRead.removeObservers(this)
                }
            }
        }
    }
}