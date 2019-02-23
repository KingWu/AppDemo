package com.king.appdemo.util.ui

import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentActivity
import android.support.v4.app.FragmentManager
import com.king.appdemo.R
import java.util.*


/**
 * Created by dev-kingwu on 27/1/2017.
 */

object FragmentFlowUtil {

    fun find(fragmentManager: FragmentManager, tag: String): Fragment? {
        return fragmentManager.findFragmentByTag(tag)
    }

    @JvmOverloads
    fun switchView(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        fragment: Fragment,
        isAddBackStack: Boolean,
        layoutId: Int = R.id.layoutMain
    ) {
        switchView(fragmentManager, fragment, isAddBackStack, layoutId)
    }

    fun switchView(activity: FragmentActivity, fragmentManager: FragmentManager, fragment: Fragment) {
        FragmentFlowUtil.switchView(activity, fragmentManager, fragment, true)
    }


    fun showDialog(fragmentManager: FragmentManager, fragment: DialogFragment, tag: String) {
        val ft = fragmentManager.beginTransaction()
        val prev = fragmentManager.findFragmentByTag(tag)
        if (prev != null) {
            ft.remove(prev)
        }
        ft.addToBackStack(null)
        fragment.show(ft, tag)
    }

    private fun removeView(fragmentManager: FragmentManager, fragment: Fragment?) {
        if (null != fragment) {
            fragmentManager.beginTransaction().remove(fragment).commit()
        }
    }

    /**
     * For Mean Tab
     * @param activity
     * @param oldFragment
     * @param newFragment
     */
    fun switchView(
        activity: FragmentActivity,
        fragmentManager: FragmentManager,
        newFragment: Fragment,
        oldFragment: Fragment
    ) {
        val ft = fragmentManager.beginTransaction()

        ft.detach(oldFragment)
        ft.attach(newFragment).commit()
    }

    fun addAndDetachAllView(activity: FragmentActivity, vararg newFragments: Fragment) {
        val fragmentManager = activity.supportFragmentManager
        val ft = fragmentManager.beginTransaction()

        for (fragment in newFragments) {
            ft.add(R.id.layoutMain, fragment)
            ft.detach(fragment)
        }
        ft.commit()
    }


    //    public static void switchSubView(Fragment parentFragment, Fragment fragment, boolean isAddBackStack)
    //    {
    //        FragmentManager fragmentManager = parentFragment.getChildFragmentManager();
    //        switchView(fragmentManager, fragment, isAddBackStack, R.id.mainSubRootFrame);
    //    }

    private fun switchView(
        fragmentManager: FragmentManager,
        fragment: Fragment,
        isAddBackStack: Boolean,
        layoutID: Int
    ) {
        val ft = fragmentManager.beginTransaction()

        var tag: String? = null
        if (isAddBackStack) {
            tag = UUID.randomUUID().toString()
            //            ft.addToBackStack(null);
            ft.addToBackStack(tag)
        }

        if (null != tag) {
            ft.replace(layoutID, fragment, tag)
                .commit()
        } else {
            ft.replace(layoutID, fragment)
                .commit()
        }
    }

    fun popFragmentBackStack(activity: FragmentActivity) {
        val fragmentManager = activity.supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        if (count > 0) {
            val last = fragmentManager.getBackStackEntryAt(count - 1)
            val fragment = fragmentManager.findFragmentByTag(last.name)

            fragmentManager.popBackStack(last.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)

            removeView(fragmentManager, fragment)
        }

    }

    fun popFragmentBackStack(activity: FragmentActivity, pClass: Class<*>) {
        val fragmentManager = activity.supportFragmentManager
        val count = fragmentManager.backStackEntryCount
        var lastId = 0
        var i = count
        while (--i >= 0) {
            val last = fragmentManager.getBackStackEntryAt(i)
            val fragment = fragmentManager.findFragmentByTag(last.name)

            if (pClass.isInstance(fragment)) {
                fragmentManager.popBackStack(lastId, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                break
            }

            lastId = last.id
        }
    }

    fun clearBackStack(activity: FragmentActivity) {
        val fragmentManager = activity.supportFragmentManager
        if (fragmentManager.backStackEntryCount > 0) {
            val first = fragmentManager.getBackStackEntryAt(0)
            fragmentManager.popBackStack(first.id, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        }
    }

    fun getVisibleFragment(fragmentManager: FragmentManager): Fragment? {
        val fragments = fragmentManager.fragments
        for (fragment in fragments) {
            if (fragment != null && fragment.isVisible)
                return fragment
        }
        return null
    }
}