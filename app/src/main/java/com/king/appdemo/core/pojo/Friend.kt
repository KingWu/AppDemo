package com.king.appdemo.core.pojo

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Friend : RealmObject() {
    @PrimaryKey
    var _id: String? = null
    var picture: String? = null
    var name: String? = null
    var email: String? = null
    var location: Location? = null
}
