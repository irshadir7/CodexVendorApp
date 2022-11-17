package com.codex.ventorapp.main.inventory_adjustment.intent

sealed class InventoryAdjustmentIntent {
    class GetInventoryAdjustment(val token: String) : InventoryAdjustmentIntent()
    class GetBarCodeProduct(val token: String, val id: String,val  locationId: String) : InventoryAdjustmentIntent()
    class GetUpdateInventory(val token: String,val product_id: String,val location_id: String,val stock_quant_id: String,val inventory_quantity: String, val isPackage:Boolean , val packageId:Int) : InventoryAdjustmentIntent()
}