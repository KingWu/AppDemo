package com.king.appdemo.vm

import com.king.appdemo.core.pojo.Friend
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.subjects.BehaviorSubject

class DetailViewModel{
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var getFriend = BehaviorSubject.create<Friend>()

    fun loadFriend(){
        // TODO: load DB
    }

    fun dispose(){
        compositeDisposable.dispose()
    }
}