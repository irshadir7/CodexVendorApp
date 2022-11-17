package com.codex.ventorapp.main.onboarding.intent

sealed class DatabaseIntent {
    object GetDataBase : DatabaseIntent()
}