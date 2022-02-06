package com.demo.end.main.model

data class EndProductsModel(
    val product_count: Int,
    var products: List<Product>,
    val title: String
)