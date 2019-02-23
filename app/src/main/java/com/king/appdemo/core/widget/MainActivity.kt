package com.king.appdemo.core.widget

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.king.appdemo.R
import com.king.appdemo.ui.FriendListFragment
import com.king.appdemo.util.ui.FragmentFlowUtil

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_root)
        FragmentFlowUtil.switchView(this, supportFragmentManager, FriendListFragment(), false)
    }
}
