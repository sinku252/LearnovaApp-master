package com.sambhav.tws.ui.home.home.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemNotificationListBinding
import com.sambhav.tws.ui.home.home.model.Notification
import com.sambhav.tws.utils.ACTION_VIEW
import com.sambhav.tws.utils.CustomClickListener
import getChatTime

class NotificationListAdapter(
    private val mContext : Context,
    var mList : ArrayList<Notification>,
    val mCallback: BaseCallback
) :  BaseBindingAdapter(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ViewHolderMenu(
                ItemNotificationListBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        return binding
    }

    inner class ViewHolderMenu(val binding : ItemNotificationListBinding) : BaseViewHolder(binding.root){
        override fun bindData(position: Int) {
            binding.notification = mList[position]
            binding.tvDate.text = mList[position].created_date.getChatTime()
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })
        }
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    interface Callback {
        abstract fun onItemClick(position: Int)
    }
}