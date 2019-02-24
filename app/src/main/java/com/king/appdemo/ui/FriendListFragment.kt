package com.king.appdemo.ui

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.king.appdemo.R
import com.king.appdemo.core.widget.BaseFragment
import com.king.appdemo.ui.adapter.FriendListAdapter
import com.king.appdemo.util.ui.OnItemClickListener
import com.king.appdemo.vm.FriendListViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_list.*


class FriendListFragment : BaseFragment(){

    var toolBar: Toolbar? = null
    var adapter: FriendListAdapter? = null
    lateinit var viewModel: FriendListViewModel
    var compositeDisposable: CompositeDisposable = CompositeDisposable()

    var onItemClick = object: OnItemClickListener{
        override fun onItemClick(view: View, pos: Int) {
            viewModel.handleItemClick(activity, pos)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = FriendListViewModel(getAppEngine())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        init()
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        viewModel.dispose()
        super.onDestroy()
    }

    private fun initUI(){

        var appCompatActivity: AppCompatActivity = activity as AppCompatActivity

        toolBar = appCompatActivity.findViewById(R.id.toolbar)
        toolBar?.title = getString(R.string.titleFriendList)

        recyclerView.layoutManager = LinearLayoutManager(activity)
        val itemDecorator = DividerItemDecoration(context!!, DividerItemDecoration.VERTICAL)
        itemDecorator.setDrawable(ContextCompat.getDrawable(context!!, R.drawable.item_divider)!!)
        recyclerView.addItemDecoration(itemDecorator)
    }

    private fun init(){
        if(null == adapter){
            viewModel.init()
            listenEvent()
        }
        else{
            recyclerView.adapter = adapter
        }
    }

    private fun listenEvent(){
        var disposableGetList: Disposable = viewModel.loadFriend.subscribe {
            if(null == adapter){
                adapter = FriendListAdapter().apply {
                    friendList = it
                    onItemClickListener = onItemClick
                }
                recyclerView.adapter = adapter
            }
            else{
                adapter?.notifyDataSetChanged()
            }
        }

        var disposableError: Disposable = viewModel.showError.subscribe {
            it.printStackTrace()
            Toast.makeText(context, R.string.errorNetwork, Toast.LENGTH_SHORT).show()
        }
        compositeDisposable.add(disposableGetList)
        compositeDisposable.add(disposableError)

    }
}