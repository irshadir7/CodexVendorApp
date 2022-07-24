package com.codex.ventorapp.main.location.intent

sealed class LocationIntent {
    class GetLocation(val token: String) : LocationIntent()
}