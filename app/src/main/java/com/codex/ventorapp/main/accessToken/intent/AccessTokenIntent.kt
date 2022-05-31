package com.codex.ventorapp.main.accessToken.intent

sealed class AccessTokenIntents {
    class GetAccessToken(val client_id: String, val client_secret: String, val grant_type: String) : AccessTokenIntents()
}