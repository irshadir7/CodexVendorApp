package com.codex.ventorapp.main.onboarding.model

data class DatabaseList(
    val id: Any,
    val jsonrpc: String,
    val result: List<String>
)