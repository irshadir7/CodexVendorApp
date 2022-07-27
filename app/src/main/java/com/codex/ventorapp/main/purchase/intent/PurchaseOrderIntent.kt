package com.codex.ventorapp.main.purchase.intent

sealed class PurchaseOrderIntent {
    class GetPurchaseOrder(val token: String) : PurchaseOrderIntent()
    class GetCustomerList(val token: String) : PurchaseOrderIntent()
}