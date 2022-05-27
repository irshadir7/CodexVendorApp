package com.codex.ventorapp.main.login.model

data class LoginSuccessData(
    val access_token: String,
    val code: Int,
    val expires_in: Int,
    val message: String,
    val scope: String,
    val status: String,
    val token_type: String
)