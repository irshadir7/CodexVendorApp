package com.codex.ventorapp.main.inventory_adjustment.model

data class BarCodeList(
    val code: Int,
    val message: String,
    val result: ResultBarCode,
    val status: String
)

data class ResultBarCode(
    val cost: Double,
    val id: Int,
    val internal_reference: Boolean,
    val invoice_policy: String,
    val is_package: Boolean,
    val name: String,
    val package_id:Boolean,
    val product_category: String,
    val product_category_id: Int,
    val product_type: String,
    val qty_available: Double,
    val sale_price: Double
)