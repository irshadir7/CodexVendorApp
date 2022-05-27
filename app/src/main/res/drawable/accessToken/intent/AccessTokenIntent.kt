package com.codex.ventorapp.main.accessToken.intent

sealed class AccessTokenIntent {
    class GetAccessToken(val client_id: String, val client_secret: String, val grant_type: String) : AccessTokenIntent()
}