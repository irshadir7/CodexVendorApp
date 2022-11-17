package com.codex.ventorapp.foundatiion

import android.app.Application
import com.codex.ventorapp.di.NetworkModule
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BaseApplication :Application() {

    override fun onCreate() {
        super.onCreate()
        NetworkModule.application = applicationContext as Application
    }
}