package com.codex.ventorapp.main.inventory_adjustment.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.codex.ventorapp.R
import com.codex.ventorapp.main.inventory_adjustment.model.ResultAdjustment

class TableViewAdapter(var context: Context) :
    RecyclerView.Adapter<TableViewAdapter.RowViewHolder>() {

    private var dataList = emptyList<ResultAdjustment>()

    internal fun setDataList(dataList: List<ResultAdjustment>) {
        this.dataList = dataList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RowViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.table_list_item, parent, false)
        return RowViewHolder(itemView)
    }

    private fun setHeaderBg(view: View) {
        view.setBackgroundResource(R.drawable.table_header_cell_bg)
    }

    private fun setContentBg(view: View, rowPos: Int) {


        if (rowPos % 2 == 1) {
            view.setBackgroundResource(R.drawable.table_content_cell_bg)
        } else {
            view.setBackgroundResource(R.drawable.table_content_second_cell_bg)

        }

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RowViewHolder, position: Int) {
        val rowPos = holder.adapterPosition

        if (rowPos == 0) {
            holder.apply {
                setHeaderBg(productName)
                setHeaderBg(barcodeNumber)
                setHeaderBg(inventoryQuantity)
                productName.text = "Item"
                barcodeNumber.text = "Barcode"
                inventoryQuantity.text = "Counted"
                productName.setTypeface(productName.typeface, Typeface.BOLD)
                barcodeNumber.setTypeface(barcodeNumber.typeface, Typeface.BOLD)
                inventoryQuantity.setTypeface(inventoryQuantity.typeface, Typeface.BOLD)
                productName.setTextColor(context.resources.getColor(R.color.white))
                barcodeNumber.setTextColor(context.resources.getColor(R.color.white))
                inventoryQuantity.setTextColor(context.resources.getColor(R.color.white))

            }
        } else {
            val modal = dataList[rowPos - 1]

            holder.apply {
                setContentBg(productName, rowPos)
                setContentBg(barcodeNumber, rowPos)
                setContentBg(inventoryQuantity, rowPos)
                holder.productName.text = modal.product_name
                holder.barcodeNumber.text = modal.barcode
                holder.inventoryQuantity.text = modal.inventory_quantity.toInt().toString()
                productName.setTextColor(context.resources.getColor(R.color.black))
                barcodeNumber.setTextColor(context.resources.getColor(R.color.black))
                inventoryQuantity.setTextColor(context.resources.getColor(R.color.black))
            }
        }
    }

    override fun getItemCount(): Int {
        return dataList.size + 1 // one more to add header row
    }

    inner class RowViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName: TextView = itemView.findViewById(R.id.productName)
        var barcodeNumber: TextView = itemView.findViewById(R.id.barcodeNumber)
        var inventoryQuantity: TextView = itemView.findViewById(R.id.inventoryQuantity)
    }
}