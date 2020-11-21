package com.sambhav.tws.ui.home.videos.adapter

import android.content.Context
import android.view.ViewGroup
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemMsgReceiveBinding
import com.sambhav.tws.databinding.ItemMsgSendBinding

class OneToOneChatAdapter(
    private val mContext: Context,
    var mList: ArrayList<String>
) : BaseBindingAdapter() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if(viewType %2 ==0){
            ViewHolderReceive(
                ItemMsgReceiveBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        }else{
            ViewHolderSend(
                ItemMsgSendBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return 12
    }

    inner class ViewHolderReceive(val binding: ItemMsgReceiveBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
        }
    }

    inner class ViewHolderSend(val binding: ItemMsgSendBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {

        }
    }


    fun updateList(List: ArrayList<String>) {
        mList = List
        notifyDataSetChanged()
    }

    interface Callback {
        abstract fun onItemClick(position: Int)
    }
}