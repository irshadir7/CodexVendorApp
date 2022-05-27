package com.codex.ventorapp.main.login.intent

sealed class LoginIntent {
    class GetLogin(val login: String, val password: String) : LoginIntent()
}