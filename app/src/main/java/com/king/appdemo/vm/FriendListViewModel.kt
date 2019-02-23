package com.king.appdemo.vm

import com.king.appdemo.core.api.APIProvider
import com.king.appdemo.core.pojo.Friend
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject


class FriendListViewModel(val apiProvider: APIProvider){

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var loadFriend = BehaviorSubject.create<List<Friend>>()
    var showError = BehaviorSubject.create<Throwable>()


    fun init(){
        val disposable: Disposable = apiProvider.friendService.getFriendList()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    loadFriend.onNext(it)
                },
                {
                    showError.onNext(it)
                }
            )
        compositeDisposable.add(disposable)
    }

    fun dispose(){
        compositeDisposable.dispose()
    }
}