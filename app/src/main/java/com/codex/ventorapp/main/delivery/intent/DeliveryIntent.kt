package com.codex.ventorapp.main.delivery.intent


sealed class DeliveryIntent{
    class GetDelivery(val token: String,val partner_id: String) : DeliveryIntent()
}
