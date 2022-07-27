package com.codex.ventorapp.main.purchase.model

data class Customer(
    val code: Int,
    val message: String,
    val result: List<CustomerList>,
    val status: String
)

data class CustomerList(
    val id: Int,
    val name: String
)