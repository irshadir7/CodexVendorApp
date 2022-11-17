package com.codex.ventorapp.main.onboarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.codex.ventorapp.R
import com.codex.ventorapp.foundatiion.utilz.SessionManager
import com.codex.ventorapp.main.ui.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_on_boarding.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.InternalCoroutinesApi

@FlowPreview
@ExperimentalCoroutinesApi
@InternalCoroutinesApi
@AndroidEntryPoint
class OnBoardingActivity : AppCompatActivity() {
    private lateinit var session: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_on_boarding)
        session = SessionManager(this)
        if (session.isLoggedIn()) {
            serverUi.isVisible = false
            val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            this@OnBoardingActivity.finish()
        } else if (session.isUrlSaved() && !session.isLoggedIn()) {
            val intent = Intent(this@OnBoardingActivity, DatabaseChooseActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        } else {
            serverUi.isVisible = true

        }
        nextBtn.setOnClickListener {
            if (tvBaseUrl.text!!.isNotEmpty()) {
                val str = tvBaseUrl.text.toString().trim()
                session.saveBaseURL(str)
                val intent = Intent(this@OnBoardingActivity, DatabaseChooseActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
                Runtime.getRuntime().exit(0)
            }

        }
    }
}