package com.codex.ventorapp.main.returns.intent


sealed class ReturnIntent{
    class GetReturn(val token: String,val partner_id: String) : ReturnIntent()
}
