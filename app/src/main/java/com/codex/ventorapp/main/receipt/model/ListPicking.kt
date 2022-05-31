package com.codex.ventorapp.main.receipt.model

data class ListPicking(
    val code: Int,
    val message: String,
    val result: List<Results>,
    val status: String
)

data class Results(
    val destination_location: List<List<Any>>,
    val name: String,
    val number_of_items: Int,
    val responsible: String,
    val scheduled_date: String,
    val source_document: String,
    val source_location: List<List<Any>>,
    val suppler: String,
)