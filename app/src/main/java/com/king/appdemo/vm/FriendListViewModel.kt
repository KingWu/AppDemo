package com.king.appdemo.vm

import com.king.appdemo.core.api.APIProvider
import com.king.appdemo.core.pojo.Friend
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class FriendListViewModel(val apiProvider: APIProvider){

    fun triggerGetFriendList():io.reactivex.Observable<List<Friend>> {
        return apiProvider.friendService.getFriendList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }
}