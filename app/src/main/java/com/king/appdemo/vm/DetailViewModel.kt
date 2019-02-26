package com.king.appdemo.vm

import com.google.android.gms.maps.model.Marker
import com.king.appdemo.core.model.AppEngine
import com.king.appdemo.core.pojo.Friend
import io.reactivex.Flowable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import io.realm.Realm
import io.realm.RealmResults



class DetailViewModel(val appEngine: AppEngine){
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var addMarkers = BehaviorSubject.create<RealmResults<Friend>>()
    var updateUI = BehaviorSubject.create<Friend>()
    var focusMarkerChange = BehaviorSubject.create<FocusMarkerChange>()

    lateinit var realm: Realm
    var isMapReady: Boolean = false
    var selectedFriend: Friend? = null
    var friendList: RealmResults<Friend>? = null
    var lastMarker: Marker? = null

    fun init(id: String, isShowAll: Boolean){

        realm = Realm.getDefaultInstance()

        var query : Flowable<RealmResults<Friend>> = if(isShowAll){
            appEngine.databaseProvider.loadFriend(realm, id)
        } else{
            appEngine.databaseProvider.loadFriendList(realm)
        }
        var loadFriendDisposable: Disposable? = query.subscribe { friends ->
            friendList = friends
            selectedFriend = friends[0]
            _noticeUpdateFriendItem()
            _noticeAddMarker(friends)
        }
        compositeDisposable.add(loadFriendDisposable!!)
    }

    fun dispose(){
        compositeDisposable.dispose()
        realm.close()
    }

    fun setMapReady(){
        isMapReady = true
        _noticeAddMarker(friendList)
    }

    fun clickMarker(clickedMarker: Marker){
        var friend: Friend = clickedMarker.tag as Friend
        selectedFriend = friend
        _noticeUpdateFriendItem()


        focusMarkerChange.onNext(FocusMarkerChange(lastMarker, clickedMarker, selectedFriend))
        lastMarker = clickedMarker
    }

    fun _noticeAddMarker(friends: RealmResults<Friend>?){
        friends?.let {
            if(isMapReady && null == addMarkers.value){
                addMarkers.onNext(friends)
            }
        }

    }

    fun _noticeUpdateFriendItem(){
        selectedFriend?.let {
            updateUI.onNext(it)
        }
    }

    data class FocusMarkerChange(val oldMarker: Marker?, val newMarker: Marker?, val selectedFriend: Friend?)
}