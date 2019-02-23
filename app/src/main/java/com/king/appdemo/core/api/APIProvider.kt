package com.king.appdemo.core.api

import com.king.appdemo.core.db.DatabaseProvider
import com.king.appdemo.core.pojo.Friend
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class APIProvider(baseUrl: HttpUrl, var databaseProvider: DatabaseProvider){

    private var retrofit: Retrofit
    private var friendService: FriendService

    init {
        retrofit = createRetrofit(baseUrl)
        friendService = retrofit.create(FriendService::class.java)
    }

    fun getFriendList(): Observable<List<Friend>>{
        return friendService.getFriendList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .flatMap {
                databaseProvider.saveFriendList(it)
                Observable.just(it)
            }
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