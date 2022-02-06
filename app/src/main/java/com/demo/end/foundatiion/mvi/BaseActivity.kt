package com.demo.end.foundatiion.mvi

import android.app.ActionBar
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.demo.end.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseActivity : AppCompatActivity(){

    private var loadingDialog:Dialog?=null
    fun loading(){
        loadingDialog = Dialog(this, R.style.Theme_Design_BottomSheetDialog)
        loadingDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        loadingDialog!!.setCancelable(true)
        loadingDialog!!.setOnCancelListener {
            if (supportFragmentManager.backStackEntryCount < 1)
                finish()
            else
                supportFragmentManager.popBackStackImmediate()
        }
        loadingDialog!!.setContentView(R.layout.loading)
        val window: Window = loadingDialog!!.window!!
        window.setLayout(
                ActionBar.LayoutParams.MATCH_PARENT,
                ActionBar.LayoutParams.MATCH_PARENT
        )
        if (!this@BaseActivity.isFinishing) {
            loadingDialog?.show()
        }
    }

    fun stopLoading(){
        if (loadingDialog!=null && !this@BaseActivity.isFinishing) loadingDialog!!.dismiss()
    }
    fun showMessage( msg: String) {
        val dialog = Dialog(this)
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