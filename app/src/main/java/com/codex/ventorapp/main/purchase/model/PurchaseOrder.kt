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
    val company: Company,
    val date_order: String,
    val name: String,
    val purchase_representative: List<PurchaseRepresentative>,
    val receipt_date: Any,
    val state: String,
    val vendor: Vendor
)


data class Company(
    val id: Int,
    val name: String
)

data class PurchaseRepresentative(
    val id: Int,
    val name: String
)

data class Vendor(
    val id: Int,
    val name: String
)