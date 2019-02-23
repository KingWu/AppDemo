package com.king.appdemo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.king.appdemo.R
import com.king.appdemo.core.pojo.Friend
import com.king.appdemo.core.widget.BaseFragment
import com.king.appdemo.vm.DetailViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.item_friend.*

class DetailFragment: BaseFragment(){

    companion object Factory {
        const val USER_ID: String = "USER_ID"

        fun createInstance(id: String): DetailFragment {
            return DetailFragment().apply {
                arguments = Bundle().apply {
                    putString(USER_ID, id)
                }
            }
        }
    }

    var compositeDisposable: CompositeDisposable = CompositeDisposable()
    lateinit var viewModel: DetailViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = DetailViewModel()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadLoadFriend()
    }

    override fun onDestroy() {
        viewModel.dispose()
        compositeDisposable.dispose()
        super.onDestroy()
    }

    fun loadLoadFriend(){
        var disposableLoadFriend: Disposable = viewModel.getFriend.subscribe {
            updateUI(it)
        }
        compositeDisposable.add(disposableLoadFriend)
        viewModel.loadFriend()
    }

    fun updateUI(friend: Friend){
        txtName.text = friend.name
        Glide.with(context!!)
            .load(friend.picture)
            .apply(RequestOptions.circleCropTransform())
            .into(imgPicture)
        // TODO Load google map
    }
}