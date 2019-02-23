package com.king.appdemo.core.widget

import android.app.Application
import com.king.appdemo.core.model.AppEngine

class AppApplication: Application() {

    lateinit var appEngine: AppEngine

    override fun onCreate() {
        super.onCreate()
        appEngine = AppEngine(this)
    }


}