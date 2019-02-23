package com.king.appdemo.core.model

import android.content.Context
import com.king.appdemo.R
import com.king.appdemo.core.api.APIProvider
import com.king.appdemo.core.db.DatabaseProvider
import io.realm.Realm
import okhttp3.HttpUrl

class AppEngine(context: Context){
    var apiProvider: APIProvider
    var databaseProvider: DatabaseProvider

    init {
        val baseUrl = context.getString(R.string.baseUrl)
        databaseProvider = DatabaseProvider(context)
        apiProvider = APIProvider(HttpUrl.get(baseUrl), databaseProvider)
    }

}