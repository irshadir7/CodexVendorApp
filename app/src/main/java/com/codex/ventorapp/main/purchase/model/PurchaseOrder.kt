package com.codex.ventorapp.main.purchase.model

data class PurchaseOrder(
    val code: Int,
    val message: String,
    val result: List<ResultPurchase>,
    val status: String
)

data class ResultPurchase(
    val amount_total: Double,
    val amount_untaxed: Double,
    val date_order: String,
    val name: String,
    val purchase_representative: List<List<Any>>,
    val receipt_date: Any
)