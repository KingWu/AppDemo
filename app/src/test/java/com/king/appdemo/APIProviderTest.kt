package com.king.appdemo

import android.content.Context
import com.king.appdemo.core.api.APIProvider
import com.king.appdemo.core.db.DatabaseProvider
import com.king.appdemo.core.pojo.Friend
import io.reactivex.observers.TestObserver
import okhttp3.HttpUrl
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock


class APIProviderTest {

    val baseUrl: String = "http://www.json-generator.com/api/"

    @Before
    fun setUp() {

    }

    @Test
    fun testGetFriendListReturnSuccess(){
        val context = mock(Context::class.java)
        var databaseProvider: DatabaseProvider = DatabaseProvider(context)
        var apiProvider = APIProvider(HttpUrl.get(baseUrl), databaseProvider)

        val mSubscriber: TestObserver<List<Friend>> = TestObserver()
        apiProvider.getFriendList().subscribe(mSubscriber)

        mSubscriber.assertNoErrors()
        mSubscriber.assertComplete()
    }
}