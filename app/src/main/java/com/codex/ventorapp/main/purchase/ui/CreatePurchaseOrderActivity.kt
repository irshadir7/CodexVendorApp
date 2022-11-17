package com.codex.ventorapp.main.purchase.ui

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.Nullable
import androidx.core.view.isVisible
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.mvi.BaseActivity
import com.codex.ventorapp.foundatiion.utilz.DataState
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.inventory_adjustment.intent.InventoryAdjustmentIntent
import com.codex.ventorapp.main.inventory_adjustment.model.BarCodeList
import com.codex.ventorapp.main.inventory_adjustment.ui.BarCodeScanActivity
import com.codex.ventorapp.main.inventory_adjustment.vm.InventoryAdjustmentViewModel
import com.codex.ventorapp.main.purchase.intent.PurchaseOrderIntent
import com.codex.ventorapp.main.purchase.model.Customer
import com.codex.ventorapp.main.purchase.vm.PurchaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_purchase_order.*
import kotlinx.android.synthetic.main.activity_inventory_adjustment.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.math.roundToInt


@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class CreatePurchaseOrderActivity : BaseActivity(), OnDateSetListener {
    @Inject
    lateinit var purchaseViewModel: PurchaseViewModel

    @Inject
    lateinit var inventoryListViewModel: InventoryAdjustmentViewModel

    private lateinit var session: SessionManager
    private var tokenValue = ""
    private var scannedValue = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_purchase_order)
        session = SessionManager(this)
        val user: HashMap<String, String> = session.getUserDetails()
        tokenValue = user[SessionManager.USER_TOKEN]!!
        authList(tokenValue)
        setUI()
        tvRDate.setOnClickListener {
            setupDatePicker()

        }
        barcodeScan.setOnClickListener {
            val intents = Intent(this, ScanActivity::class.java)
            startActivity(intents)
        }
        scannedValue = intent.getStringExtra("scannedValue").toString()
        if (scannedValue.isNotEmpty()) {
            authBarCode(scannedValue)
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun setUI() {
        val sdf = SimpleDateFormat("dd.MM.yyyy")
        val currentDate = sdf.format(Date())
        tvDate.text = currentDate
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

    private fun setupDatePicker() {
        val mDatePickerDialogFragment = DatePicker()
        mDatePickerDialogFragment.show(supportFragmentManager, "DATE PICK")

    }

    @SuppressLint("SetTextI18n")
    override fun onDateSet(p0: android.widget.DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        tvRDate.text = "$dayOfMonth.$month.$year"
    }


    private fun authBarCode(scannedValue: String) {
        lifecycleScope.launch {
            inventoryListViewModel.userIntent.send(
                InventoryAdjustmentIntent.GetBarCodeProduct(
                    tokenValue,
                    scannedValue,
                    ""
                )
            )
        }

        subscribeObserversBarcode()
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
        tvItemScannedCode.setText(scannedValue)
        tvPrice.text = data.result.sale_price.toString()
        tvQty.text = data.result.qty_available.toString()
        Log.d("ScannedValue", data.toString())

    }
}


class DatePicker : DialogFragment() {
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val mCalendar = Calendar.getInstance()
        val year = mCalendar[Calendar.YEAR]
        val month = mCalendar[Calendar.MONTH]
        val dayOfMonth = mCalendar[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(
            requireContext(),
            requireContext() as OnDateSetListener?,
            year,
            month,
            dayOfMonth
        )
    }
}