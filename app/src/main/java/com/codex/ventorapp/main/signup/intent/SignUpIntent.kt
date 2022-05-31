package com.codex.ventorapp.main.signup.intent

sealed class SignUpIntent {
    class GetSignUP(val token: String,val email: String,val name: String, val password: String) : SignUpIntent()
}