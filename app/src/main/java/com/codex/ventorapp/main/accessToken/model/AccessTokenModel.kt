package com.codex.ventorapp.main.accessToken.model

data class AccessTokenModel(
    val access_token: String,
    val expires_in: Int,
    val scope: String,
    val token_type: String
)