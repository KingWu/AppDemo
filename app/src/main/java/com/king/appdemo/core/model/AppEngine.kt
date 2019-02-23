package com.king.appdemo.core.model

import android.content.Context
import com.king.appdemo.R
import com.king.appdemo.core.api.APIProvider
import okhttp3.HttpUrl

class AppEngine(context: Context){
    var apiProvider: APIProvider

    init {
        val baseUrl = context.getString(R.string.baseUrl)
        apiProvider = APIProvider(HttpUrl.get(baseUrl))
    }

}