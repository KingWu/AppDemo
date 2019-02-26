package com.king.appdemo.core.db

import android.content.Context
import com.king.appdemo.core.pojo.Friend
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.RealmResults

class DatabaseProvider(context: Context) {

    companion object {
        const val SCHEMA_VERSION: Long = 1
    }

    init {
        Realm.init(context)
        val config = RealmConfiguration.Builder()
            .schemaVersion(SCHEMA_VERSION)
            .build()

        Realm.setDefaultConfiguration(config)
    }

    fun saveFriendList(friendList: List<Friend>) {
        val realm = Realm.getDefaultInstance()
        realm.executeTransaction { realm: Realm ->
            realm.copyToRealmOrUpdate(friendList)
            realm.close()
        }
    }

    fun loadFriendList(realm: Realm): Flowable<RealmResults<Friend>> {
        return realm.where(Friend::class.java).findAllAsync().asFlowable().filter{
            it.isLoaded && it.isValid
        }
    }

    fun loadFriend(realm: Realm, id: String): io.reactivex.Flowable<RealmResults<Friend>> {
        return realm.where(Friend::class.java)
            .equalTo("_id", id)
            .findAllAsync()
            .asFlowable()
            .filter{
                it.isLoaded && it.isValid
            }
    }
}