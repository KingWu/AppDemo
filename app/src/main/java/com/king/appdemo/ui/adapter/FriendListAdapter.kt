package com.king.appdemo.ui.adapter

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.king.appdemo.R
import com.king.appdemo.core.pojo.Friend
import kotlinx.android.synthetic.main.item_friend.view.*

class FriendListAdapter :
    RecyclerView.Adapter<FriendListAdapter.ViewHolder>(){

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view)

    var friendList: List<Friend>? = null

    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_friend, parent, false))
    }

    override fun getItemCount(): Int {
        return friendList?.size ?: 0
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val friend = friendList?.get(position)
        val itemView = viewHolder.itemView

        var topMargin: Float = 0f
        val params = viewHolder.itemView.layoutParams as RecyclerView.LayoutParams

        if(0 == position){
            topMargin = itemView.context.resources.getDimension(R.dimen.itemDivider)
        }

        params.setMargins(0, topMargin.toInt(), 0, 0)

        Glide.with(itemView)
            .load(friend?.picture)
            .apply(RequestOptions.circleCropTransform())
            .into(itemView.imgPicture)
        itemView.txtName.text = friend?.name
    }

}