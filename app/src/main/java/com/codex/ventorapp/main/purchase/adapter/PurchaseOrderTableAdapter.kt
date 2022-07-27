package com.codex.ventorapp.main.purchase.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.recyclerview.widget.RecyclerView
import com.codex.ventorapp.R
import com.codex.ventorapp.main.purchase.model.ResultPurchase
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


class PurchaseOrderTableAdapter(var context: Context) :
    RecyclerView.Adapter<PurchaseOrderTableAdapter.RowViewHolder>() {

    private var dataList = emptyList<ResultPurchase>()

    internal fun setDataList(dataList: List<ResultPurchase>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_purchase, parent, false)
        return RowViewHolder(itemView)
    }


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {


        holder.name.text = dataList[position].company.name
        holder.amount.text = dataList[position].amount_total.toString()+" "+" AED"
        holder.refNumber.text = dataList[position].name
        holder.tvDate.text = getDate(dataList[position].date_order)

        if (dataList[position].state == "draft") {
            holder.createBtn.text = "RFQ"
            holder.createBtn.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.green))
        } else {
            holder.createBtn.text = "Purchase Order"
            holder.createBtn.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.btn_color))


        }


    }

    @Throws(ParseException::class)
    fun getDate(startDate: String): String {
        @SuppressLint("SimpleDateFormat") val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var d: Date? = null
        try {
            d = sdf.parse(startDate)
            sdf.applyPattern("MMMM dd, YYYY") //this gives output as=> "Month date, year"
        } catch (e: Exception) {

        }
        return sdf.format(d!!)
    }

    override fun getItemCount(): Int {
        return dataList.size // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.name)
        var amount: TextView = itemView.findViewById(R.id.amount)
        var refNumber: TextView = itemView.findViewById(R.id.refNumber)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var createBtn: AppCompatButton = itemView.findViewById(R.id.createBtn)
    }
}