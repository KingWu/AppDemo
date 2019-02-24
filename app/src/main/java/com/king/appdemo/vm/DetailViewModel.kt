package com.king.appdemo.vm

import android.util.Log
import com.king.appdemo.core.model.AppEngine
import com.king.appdemo.core.pojo.Friend
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm

class DetailViewModel(val appEngine: AppEngine){
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var getFriend = BehaviorSubject.create<Friend>()
    lateinit var realm: Realm

    fun init(id: String){

        realm = Realm.getDefaultInstance()

        val loadFriendDisposable: Disposable = appEngine.databaseProvider.loadFriend(realm, id)
            .subscribe { it ->
                getFriend.onNext(it)
            }
        compositeDisposable.add(loadFriendDisposable)
    }

    fun dispose(){
        compositeDisposable.dispose()
        realm.close()
    }
}