package com.king.appdemo.core.api

import com.king.appdemo.core.pojo.Friend

interface FriendService{

    @retrofit2.http.GET("json/get/cfdlYqzrfS")
    fun getFriendList(): io.reactivex.Observable<List<Friend>>
}