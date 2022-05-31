package com.codex.ventorapp.main.receipt.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.codex.ventorapp.R
import com.codex.ventorapp.main.receipt.model.Results
import org.json.JSONArray
import org.json.JSONException


class ReceiptListAdapter(var context: Context) :
    RecyclerView.Adapter<ReceiptListAdapter.ViewHolder>() {

    private var dataList = emptyList<Results>()

    internal fun setDataList(dataList: List<Results>) {
        this.dataList = dataList
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvName: TextView = itemView.findViewById(R.id.tvName)
        var tvDate: TextView = itemView.findViewById(R.id.tvDate)
        var tvStock: TextView = itemView.findViewById(R.id.tvStock)
        var tvSupplier: TextView = itemView.findViewById(R.id.tvSupplier)
        var txtAdmin: TextView = itemView.findViewById(R.id.txtAdmin)
        var txtLPO: TextView = itemView.findViewById(R.id.txtLPO)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.receipt_item_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.tvName.text = data.name
        holder.tvDate.text = data.scheduled_date
        holder.tvSupplier.text = data.suppler

        if (data.source_document.isEmpty()) {
            holder.txtLPO.isVisible = false
        } else {
            holder.txtLPO.text = data.source_document
        }
        if (data.responsible.isEmpty()) {
            holder.txtAdmin.isVisible = false
        } else {
            holder.txtAdmin.text = data.responsible
        }
        try {
            val itemArray = JSONArray(data.source_location[0])
            for (i in 0 until itemArray.length()) {
                val value: String = itemArray.getString(i)
                holder.tvStock.text = value
            }
        } catch (e: JSONException) {
            e.printStackTrace()
        }


    }

    override fun getItemCount() = dataList.size
}