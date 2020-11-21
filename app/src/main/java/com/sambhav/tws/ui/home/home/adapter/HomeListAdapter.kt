package com.sambhav.tws.ui.home.home.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemHomeSubBinding
import com.sambhav.tws.ui.home.home.model.MenuModel
import com.sambhav.tws.utils.CustomClickListener

class HomeListAdapter(
    private val mContext : Context,
    var mList : ArrayList<MenuModel>,
    val mCallback: Callback
) :  BaseBindingAdapter(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        val binding =
            ViewHolderMenu(
                ItemHomeSubBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        return binding
    }

    inner class ViewHolderMenu(val binding : ItemHomeSubBinding) : BaseViewHolder(binding.root){
        override fun bindData(position: Int) {
            binding.menu = mList[position]
            binding.executePendingBindings()
            binding.root.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position)
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