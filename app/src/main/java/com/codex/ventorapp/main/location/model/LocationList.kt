package com.codex.ventorapp.main.location.model

data class LocationList(
    val code: Int,
    val message: String,
    val result: List<Locations>,
    val status: String
)

data class Locations(
    val id: Int,
    val name: String
)