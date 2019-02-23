package com.king.appdemo.core.widget

import android.support.v4.app.Fragment
import com.king.appdemo.core.model.AppEngine

open class BaseFragment : Fragment(){
    fun getAppEngine() : AppEngine{
        return (activity?.application as AppApplication).appEngine
    }
}