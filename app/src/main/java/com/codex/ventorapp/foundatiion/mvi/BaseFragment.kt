package com.codex.ventorapp.foundatiion.mvi

import android.app.ActionBar
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.location.LocationManager
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.codex.ventorapp.R
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
open class BaseFragment(layout:Int):Fragment(layout){

    var country = ""
    var city = ""
    var loadingDialog:Dialog?=null
    var onForground = false


    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    fun Long.toShortCutNumber():String{
        var thousands=0L
        var hundred=0L
        if(this>1000) {
            thousands = this / 1000
            hundred   = (this % 1000) / 100
        }
        var million=0L
        var hundredOfThousands =0L
        if (this/1000>1000) {
            million            = this / 1000000
            hundredOfThousands = (this % 1000000) / 100000
        }
        if (million==0L&&thousands==0L)
            return this.toString()
        else{
            if (million>0)
                return "$million"+".$hundredOfThousands M"
            else
                return "$thousands"+".$hundred K"
        }

    }
 /*   fun String.toUrl():String{
        return BuildConfig.MEDIA_URL+this
    }

    fun String.toAmazonUrl():String{
        return BuildConfig.MEDIA_URL+this
    }*/

    fun loading(){
        loadingDialog = Dialog(activityContext(), R.style.Theme_Design_BottomSheetDialog)
        loadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.setCancelable(true)
        loadingDialog!!.setContentView(R.layout.loading)
        val window: Window = loadingDialog!!.window!!
        window.setLayout(
            ActionBar.LayoutParams.MATCH_PARENT,
            ActionBar.LayoutParams.MATCH_PARENT
        )
        if (!requireActivity().isFinishing) {
            loadingDialog?.show()
        }

    }
    fun stopLoading(){
        if (loadingDialog!=null && !requireActivity().isFinishing) loadingDialog!!.dismiss()
    }

    fun activityContext(): Context {
        val context = context
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context!!
    }

    fun showMessage( msg: String) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        val text = dialog.findViewById(R.id.text_dialog) as TextView
        text.text = msg
        val dialogButton: Button = dialog.findViewById(R.id.btn_dialog) as Button
        dialogButton.setOnClickListener{ dialog.dismiss() }
        dialog.show()
    }

}
