package com.king.appdemo.vm

import android.support.v4.app.FragmentActivity
import android.util.Log
import com.king.appdemo.core.api.APIProvider
import com.king.appdemo.core.pojo.Friend
import com.king.appdemo.ui.DetailFragment
import com.king.appdemo.util.ui.FragmentFlowUtil
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

    fun handleItemClick(acticity: FragmentActivity?, position: Int){
        Log.d("test", "pos : " + position)
        acticity?.let {

            FragmentFlowUtil.switchView(
                acticity,
                acticity.supportFragmentManager,
                DetailFragment.createInstance(loadFriend.value?.get(position)?._id!!)
            )
        }
    }

    fun dispose(){
        compositeDisposable.dispose()
    }
}