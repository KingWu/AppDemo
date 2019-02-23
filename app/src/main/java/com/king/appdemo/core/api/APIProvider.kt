package com.king.appdemo.core.api

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class APIProvider(baseUrl: HttpUrl){

    private var retrofit: Retrofit
    var friendService: FriendService

    init {
        retrofit = createRetrofit(baseUrl)
        friendService = retrofit.create(FriendService::class.java)
    }

    private fun createRetrofit(baseUrl:HttpUrl) : Retrofit{
        return retrofit2.Retrofit.Builder()
            .addCallAdapterFactory(retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory.create())
            .addConverterFactory(retrofit2.converter.gson.GsonConverterFactory.create())
            .client(createOkHttp())
            .baseUrl(baseUrl)
            .build()
    }

    private fun createOkHttp(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
            .addInterceptor(interceptor)

        return builder.build()
    }

}