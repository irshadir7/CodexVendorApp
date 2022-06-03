package com.codex.ventorapp.main.inventory_adjustment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.codex.ventorapp.R
import com.codex.ventorapp.main.inventory_adjustment.model.ResultAdjustment
import com.codex.ventorapp.main.receipt.model.Results
import org.json.JSONArray
import org.json.JSONException


class InventoryListAdapter(var context: Context) :
    RecyclerView.Adapter<InventoryListAdapter.ViewHolder>() {

    private var dataList = emptyList<ResultAdjustment>()

    internal fun setDataList(dataList: List<ResultAdjustment>) {
        this.dataList = dataList
    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName: TextView = itemView.findViewById(R.id.product_name)
        var inventoryDate: TextView = itemView.findViewById(R.id.inventory_date)
        var location: TextView = itemView.findViewById(R.id.location)
        var onHandQuantity: TextView = itemView.findViewById(R.id.onhand_quantity)
        var inventoryQuantity: TextView = itemView.findViewById(R.id.inventory_quantity)


    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.inventory_item_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = dataList[position]
        holder.productName.text = data.product_name
        holder.inventoryDate.text = data.inventory_date
        holder.location.text = data.location
        holder.onHandQuantity.text = "On hand Quantity: "+data.onhand_quantity
        holder.inventoryQuantity.text ="Available Quantity: "+ data.inventory_quantity

    }

    override fun getItemCount() = dataList.size
}