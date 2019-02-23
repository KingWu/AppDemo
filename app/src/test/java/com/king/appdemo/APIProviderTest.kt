package com.king.appdemo

import com.king.appdemo.core.api.APIProvider
import com.king.appdemo.core.pojo.Friend
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import org.junit.Before
import org.junit.Test


class APIProviderTest {

    val baseUrl: String = "http://www.json-generator.com/api/"

    @Before
    fun setUp() {

    }

    @Test
    fun testGetFriendListReturnSuccess(){
        var apiProvider = APIProvider(HttpUrl.get(baseUrl))

        val mSubscriber: TestObserver<List<Friend>> = TestObserver()
        apiProvider.friendService.getFriendList().subscribe(mSubscriber)

        mSubscriber.assertNoErrors()
        mSubscriber.assertComplete()
    }
}