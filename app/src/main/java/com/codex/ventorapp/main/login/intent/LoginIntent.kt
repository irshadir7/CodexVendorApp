package com.codex.ventorapp.main.login.intent

sealed class LoginIntent {
    class GetLogin(val db: String,val login: String, val password: String) : LoginIntent()
}