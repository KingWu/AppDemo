package com.king.appdemo.vm

import android.support.v4.app.FragmentActivity
import com.king.appdemo.core.model.AppEngine
import com.king.appdemo.core.pojo.Friend
import com.king.appdemo.ui.DetailFragment
import com.king.appdemo.util.ui.FragmentFlowUtil
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import io.realm.RealmChangeListener
import io.realm.RealmResults


class FriendListViewModel(val appEngine: AppEngine){

    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var loadFriend = BehaviorSubject.create<List<Friend>>()
    var showError = BehaviorSubject.create<Throwable>()
    lateinit var realm: Realm


    fun init(){

        realm = Realm.getDefaultInstance()
        val disposable: Disposable = appEngine.apiProvider.getFriendList()
            .subscribe(
                {
//                    loadFriend.onNext(it)
                },
                {
                    showError.onNext(it)
                }
            )

        val loadFriendDisposable: Disposable = appEngine.databaseProvider.loadFriendList(realm)
            .subscribe { it ->
                it.addChangeListener(RealmChangeListener<RealmResults<Friend>> {
                    loadFriend.onNext(it)
                })
                loadFriend.onNext(it)
            }
        compositeDisposable.add(disposable)
        compositeDisposable.add(loadFriendDisposable)
    }

    fun handleItemClick(acticity: FragmentActivity?, position: Int){
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
        realm.close()
    }
}