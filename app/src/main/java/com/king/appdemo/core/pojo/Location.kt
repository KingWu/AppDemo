package com.king.appdemo.core.pojo

import io.realm.RealmObject

open class Location : RealmObject(){
    var latitude: Double? = null
    var longitude: Double? = null
}