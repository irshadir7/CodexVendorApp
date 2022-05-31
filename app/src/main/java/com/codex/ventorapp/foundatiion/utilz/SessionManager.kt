package com.codex.ventorapp.foundatiion.utilz

import android.content.Context
import android.content.SharedPreferences
import com.codex.ventorapp.main.accessToken.model.AccessTokenModel
import com.codex.ventorapp.main.login.model.LoginSuccessData
import kotlin.collections.HashMap


class SessionManager(var context: Context) {

    private var pref: SharedPreferences
    private var editor: SharedPreferences.Editor
    var privateMode: Int = 0


    init {
        pref = context.getSharedPreferences(PREF_NAME, privateMode)
        editor = pref.edit()
    }

    companion object {

        const val PREF_NAME: String = "Vendor"
        const val IS_LOGIN: String = "isLogin"
        const val PARTNER_ID: String = "partner_id"
        const val TOKEN: String = "token"
        const val TOKEN_EXPIRY: String = "token_expiry"
        const val USER_TOKEN: String = "user_token"
        const val USER_TOKEN_EXPIRY: String = "user_token_expiry"
    }

    fun createLoginSession(data: LoginSuccessData) {
        editor.putBoolean(IS_LOGIN, true)
        editor.putString(PARTNER_ID, data.partner_id)
        editor.putString(USER_TOKEN, data.access_token)
        editor.putString(USER_TOKEN_EXPIRY, data.expiration_date)
        editor.commit()
    }

    fun getTokenDetails(): HashMap<String, String> {
        val user: Map<String, String> = HashMap()
        (user as HashMap)[TOKEN] = pref.getString(TOKEN, null).toString()
        user[TOKEN_EXPIRY] = pref.getString(TOKEN_EXPIRY, null).toString()
        user[PARTNER_ID] = pref.getString(PARTNER_ID, null).toString()
        return user
    }

    fun getUserDetails(): HashMap<String, String> {
        val user: Map<String, String> = HashMap()
        (user as HashMap)[PARTNER_ID] = pref.getString(PARTNER_ID, null).toString()
        user[USER_TOKEN] = pref.getString(USER_TOKEN, null).toString()
        return user
    }


    fun isLoggedIn(): Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }


    fun saveTokenInstance(data: AccessTokenModel) {
        editor.putString(TOKEN, data.access_token)
        editor.putString(TOKEN_EXPIRY, data.expires_in.toString())
        editor.commit()
    }

}
