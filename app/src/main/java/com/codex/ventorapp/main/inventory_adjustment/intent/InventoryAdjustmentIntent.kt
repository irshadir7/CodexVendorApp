package com.codex.ventorapp.main.inventory_adjustment.intent

sealed class InventoryAdjustmentIntent {
    class GetInventoryAdjustment(val token: String) : InventoryAdjustmentIntent()
}