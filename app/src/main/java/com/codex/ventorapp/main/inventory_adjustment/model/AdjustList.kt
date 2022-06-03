package com.codex.ventorapp.main.inventory_adjustment.model

data class AdjustList(
    val code: Int,
    val message: String,
    val result: List<ResultAdjustment>,
    val status: String
)

data class ResultAdjustment(
    val difference: Double,
    val id: Int,
    val inventory_date: String,
    val inventory_quantity: Double,
    val location: String,
    val location_id: Int,
    val onhand_quantity: Double,
    val product_id: Int,
    val product_name: String
)