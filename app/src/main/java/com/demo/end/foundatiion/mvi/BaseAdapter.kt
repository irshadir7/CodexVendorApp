package com.demo.end.foundatiion.mvi

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.demo.end.BuildConfig
import com.demo.end.R
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import dagger.hilt.android.internal.managers.ViewComponentManager
import kotlinx.android.synthetic.main.toast_message.view.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@ExperimentalCoroutinesApi
@InternalCoroutinesApi
abstract class BaseAdapter(var context1: Context) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){

  /*  fun String.toUrl():String{
        return BuildConfig.MEDIA_URL+this
    }*/


    fun Double.toPeriod():String{
        var minutes=(this/60).toInt().toString()
        var seconds=(this%60).toInt().toString()
        if(minutes.length==1)
            minutes="0"+minutes
        if(seconds.length==1)
            seconds="0"+seconds
        return minutes+":"+seconds
    }

    fun showMessage(message:String){
        val dialog = Dialog(context1)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog)
        val text = dialog.findViewById(R.id.text_dialog) as TextView
        text.text = message
        val dialogButton: Button = dialog.findViewById(R.id.btn_dialog) as Button
        dialogButton.setOnClickListener{ dialog.dismiss() }
        dialog.show()
    }

    fun showMessageSuccess(message:String){
        val toast = Toast(context1)
        toast.duration = Toast.LENGTH_LONG

        val inflater =
            context1.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = inflater.inflate(R.layout.toast_message, null)
        view.message.text=message
        toast.view = view
        toast.show()
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
                return "$million.$hundredOfThousands M"
            else
                return "$thousands.$hundred K"
        }

    }
    fun activityContext(context1: Context): Context? {
        val context = context1
        return if (context is ViewComponentManager.FragmentContextWrapper) {
            context.baseContext
        } else context
    }
}