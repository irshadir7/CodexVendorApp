package com.codex.ventorapp.main.inventory_adjustment.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.mvi.BaseActivity
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.inventory_adjustment.adapter.TableViewAdapter
import com.codex.ventorapp.main.inventory_adjustment.intent.InventoryAdjustmentIntent
import com.codex.ventorapp.main.inventory_adjustment.model.AdjustList
import com.codex.ventorapp.main.inventory_adjustment.model.BarCodeList
import com.codex.ventorapp.main.inventory_adjustment.vm.InventoryAdjustmentViewModel
import com.codex.ventorapp.main.location.intent.LocationIntent
import com.codex.ventorapp.main.location.model.LocationList
import com.codex.ventorapp.main.location.vm.LocationViewModel
import com.codex.ventorapp.main.model.DataModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_inventory_adjustment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.roundToInt

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class InventoryAdjustmentActivity : BaseActivity() {
    @Inject
    lateinit var inventoryListViewModel: InventoryAdjustmentViewModel

    @Inject
    lateinit var locationViewModel: LocationViewModel

    private lateinit var inventoryListAdapter: TableViewAdapter
    private lateinit var session: SessionManager


    private var scannedValue = ""
    private var tokenValue = ""
    private var _counter = 0
    private var onHand = 0
    private var locationId = ""
    private var locationIdSelected = false
    private var productId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory_adjustment)
        val intent = intent
        scannedValue = intent.getStringExtra("scannedValue").toString()
        locationId = intent.getStringExtra("locationId").toString()
        supportActionBar?.hide()
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        tokenValue = user[SessionManager.USER_TOKEN]!!

        init()
        authList(tokenValue)
        authLocation(tokenValue)


        ivPlus.setOnClickListener {
            _counter++
            tvCount.text = _counter.toString()
            showDifferent(_counter)
        }
        ivMinus.setOnClickListener {
            if (_counter != 0) {
                _counter--
                tvCount.text = _counter.toString()
                showDifferent(_counter)
            } else {
                tvDifference.text = ""
            }

        }
        barcodeImage.setOnClickListener {

            if (locationIdSelected) {
                val intents = Intent(this, BarCodeScanActivity::class.java)
                intents.putExtra("locationId", locationId)
                startActivity(intents)
            } else {
                Toast.makeText(applicationContext, "Please choose Location", Toast.LENGTH_SHORT)
                    .show()

            }


        }

        if (scannedValue.isNotEmpty() && locationId.isNotEmpty()) {
            Log.d("LocationList", locationId)
            authBarCode(scannedValue, locationId)
        }
        btnClear.setOnClickListener {
            clearUI()
        }
        loading()
    }

    private fun showDifferent(_counter: Int) {
        val diff = _counter - onHand
        if (diff < 0) {
            tvDifference.setTextColor(Color.parseColor("#FF0000"))
            tvDifference.text = diff.toString()
        } else {
            tvDifference.setTextColor(Color.parseColor("#006400"))
            tvDifference.text = diff.toString()
        }

    }

    private fun init() {
        inventoryListAdapter = TableViewAdapter(applicationContext)
        rvInventoryList
            .setHasFixedSize(true)
        rvInventoryList.apply {
            layoutManager = LinearLayoutManager(
                context,
            )
            adapter = inventoryListAdapter
        }

    }

    private fun authBarCode(scannedValue: String, locationId: String) {
        lifecycleScope.launch {
            inventoryListViewModel.userIntent.send(
                InventoryAdjustmentIntent.GetBarCodeProduct(
                    tokenValue,
                    scannedValue,
                    locationId
                )
            )
        }

        subscribeObserversBarcode()
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
                    stopLoading()
                }
                is DataState.Error -> {
                    Log.d("AdjustList", adjustListRead.exception.toString())
                    inventoryListViewModel.adjustListRead.removeObservers(this)
                    stopLoading()
                }
                is DataState.ServerError->{
                    stopLoading()
                }
                is DataState.Loading -> {

                }
                else -> {
                    stopLoading()
                    inventoryListViewModel.adjustListRead.removeObservers(this)
                }
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun subscribeObserversBarcode() {
        inventoryListViewModel.barCodeRead.observe(
            this
        ) { barCodeRead ->
            when (barCodeRead) {
                is DataState.Success<BarCodeList> -> {
                    Log.d("BarCodeList", barCodeRead.data.toString())
                    inventoryListViewModel.barCodeRead.removeObservers(this)
                    setBarCodeOutputUI(barCodeRead.data)
                    stopLoading()
                }
                is DataState.Error -> {
                    Log.d("BarCodeList", barCodeRead.exception.toString())
                    inventoryListViewModel.barCodeRead.removeObservers(this)
                    stopLoading()
                }
                is DataState.ServerError -> {
                    Log.d("BarCodeList", barCodeRead.response.toString())
                    // Toast.makeText(applicationContext, barCodeRead.response.message, Toast.LENGTH_SHORT).show()
                    inventoryListViewModel.barCodeRead.removeObservers(this)
                    stopLoading()
                }

                is DataState.Loading -> {

                }
                else -> {
                    stopLoading()
                    inventoryListViewModel.barCodeRead.removeObservers(this)
                }
            }
        }
    }

    private fun setBarCodeOutputUI(data: BarCodeList) {
        tvItemCode.text = scannedValue
        tvItemName.text = data.result.name
        tvCost.text = data.result.cost.toString()
        btnLayout.isVisible = true
        llCounter.isVisible = true
        productId = data.result.id.toString()
        sale_price.text = data.result.sale_price.toString()
        qty_available.text = data.result.qty_available.toString()
        onHand = data.result.qty_available.roundToInt()
        authLocation(tokenValue)
    }


    private fun authUpdate(tokenValue: String, locationId: String) {
        lifecycleScope.launch {
            inventoryListViewModel.userIntent.send(
                InventoryAdjustmentIntent.GetUpdateInventory(
                    tokenValue, productId, locationId, "", _counter.toString()
                )
            )
        }

        subscribeUpdateObservers()
    }

    private fun subscribeUpdateObservers() {
        inventoryListViewModel.inventoryAdjustmentRead.observe(
            this
        ) { inventoryAdjustmentRead ->
            when (inventoryAdjustmentRead) {
                is DataState.Success<DataModel> -> {
                    Log.d("InventoryUpdate", inventoryAdjustmentRead.data.toString())
                    stopLoading()
                    inventoryListViewModel.inventoryAdjustmentRead.removeObservers(this)
                    Toast.makeText(applicationContext, "Stock Updated", Toast.LENGTH_SHORT).show()
                    clearUI()
                }
                is DataState.ServerError -> {
                    stopLoading()
                    Log.d("InventoryUpdate", inventoryAdjustmentRead.response.message)
                    inventoryListViewModel.inventoryAdjustmentRead.removeObservers(this)
                }
                is DataState.Error -> {
                    stopLoading()
                    Log.d("InventoryUpdate", inventoryAdjustmentRead.exception.toString())
                    inventoryListViewModel.inventoryAdjustmentRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    stopLoading()
                    inventoryListViewModel.inventoryAdjustmentRead.removeObservers(this)
                }
            }
        }
    }

    private fun authLocation(tokenValue: String) {
        lifecycleScope.launch {
            locationViewModel.userIntent.send(
                LocationIntent.GetLocation(
                    tokenValue
                )
            )
        }

        subscribeLocationObservers()
    }

    private fun clearUI() {
        tvItemCode.text = ""
        tvItemName.text = ""
        tvCost.text = ""
        sale_price.text = ""
        qty_available.text = ""
        tvDifference.text = ""
        btnLayout.isVisible = false
        llCounter.isVisible = false
        productId = ""
        _counter = 0
        tvCount.text = _counter.toString()
        init()
        authList(tokenValue)
    }

    private fun subscribeLocationObservers() {
        locationViewModel.locationDataRead.observe(
            this
        ) { locationDataRead ->
            when (locationDataRead) {
                is DataState.Success<LocationList> -> {
                    Log.d("LocationList", locationDataRead.data.toString())
                    stopLoading()
                    addToSpinner(locationDataRead.data)
                    locationViewModel.locationDataRead.removeObservers(this)

                }
                is DataState.Error -> {
                    stopLoading()
                    Log.d("LocationList", locationDataRead.exception.toString())
                    locationViewModel.locationDataRead.removeObservers(this)
                }
                is DataState.Loading -> {

                }
                else -> {
                    stopLoading()
                    locationViewModel.locationDataRead.removeObservers(this)
                }
            }
        }
    }

    private fun addToSpinner(data: LocationList) {
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
        spinnerLocation.adapter = locationAdapter

        spinnerLocation?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                Log.d("LocationList", listID[position].toString())
                locationId = listID[position].toString()
                locationIdSelected = true
                Log.d("LocationList", locationIdSelected.toString())
            }

        }

        btnApply.setOnClickListener {
            authUpdate(tokenValue, locationId)
        }

    }
}