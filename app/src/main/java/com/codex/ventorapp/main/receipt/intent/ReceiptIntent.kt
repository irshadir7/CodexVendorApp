package com.codex.ventorapp.main.receipt.intent


sealed class ReceiptIntent{
    class GetReceipt(val token: String,val partner_id: String) : ReceiptIntent()
}
