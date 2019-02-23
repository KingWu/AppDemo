package com.king.appdemo

import android.app.Application
import com.king.appdemo.core.AppEngine

class AppApplication: Application() {

    lateinit var appEngine: AppEngine

    override fun onCreate() {
        super.onCreate()
        appEngine = AppEngine(this)
    }


}