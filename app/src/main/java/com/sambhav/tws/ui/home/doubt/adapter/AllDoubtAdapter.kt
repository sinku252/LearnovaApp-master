package com.sambhav.tws.ui.home.doubt.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.apiModel.DoubtData
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemMsgImageReceiveBinding
import com.sambhav.tws.databinding.ItemMsgImageSendBinding
import com.sambhav.tws.databinding.ItemMsgReceiveBinding
import com.sambhav.tws.databinding.ItemMsgSendBinding
import com.sambhav.tws.ui.home.WebViewActivity
import com.sambhav.tws.utils.*
import getChatTime

class AllDoubtAdapter(
    private val mContext: Context,
    var mList: ArrayList<DoubtData>,
    val mCallback: BaseCallback? = null
) : BaseBindingAdapter(mContext) {

    var myId = ""
    var myType = ""

    init {
        mPref?.let {
            if (mStudent) {
                myId = getStudentData(it).id
                myType = TYPE_STUDENT
            } else {
                myId = getTeacherData(it).id
                myType = TYPE_TEACHER

            }
        }
        Log.d("AllDoubtAdapter", "myId $myId ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return if (mList[viewType].commenter_type.equals(myType, true)) {
            if (mList[viewType].attachment_type == TYPE_TEXT) {
                ViewHolderSend(
                    ItemMsgSendBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            } else {
                ViewHolderSendImage(
                    ItemMsgImageSendBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            }

        } else {
            if (mList[viewType].attachment_type == TYPE_TEXT) {
                ViewHolderReceive(
                    ItemMsgReceiveBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )

            } else {
                ViewHolderImageReceive(
                    ItemMsgImageReceiveBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            }

        }
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        super.onBindViewHolder(holder, position)
        if (position == 0) {
            mCallback?.onItemClick(0, "LOAD_MORE")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolderReceive(val binding: ItemMsgReceiveBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val doubt = mList[position]
            binding.tvMsg.text = doubt.comments
            binding.tvTime.text = doubt.created_date.getChatTime()

            binding.executePendingBindings()
        }
    }

    inner class ViewHolderImageReceive(val binding: ItemMsgImageReceiveBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val doubt = mList[position]
            if (doubt.comments == "") {
                binding.tvMsg.visibility = View.GONE
            } else {
                binding.tvMsg.visibility = View.GONE
            }
            binding.tvMsg.text = doubt.comments
            binding.tvTime.text = doubt.created_date.getChatTime()
            setDoubtImage(binding.ivImage, doubt.src_url)
            binding.root.setOnClickListener {
                val intent = Intent(mContext, WebViewActivity::class.java)
                    .putExtra(EXTRA_KEY_URL, doubt.src_url)
                    .putExtra(EXTRA_KEY_EXTENSION, FT_IMG)
                    .putExtra(EXTRA_KEY_TITLE, "")
                mContext.startActivity(intent)
            }
            binding.executePendingBindings()
        }
    }

    inner class ViewHolderSend(val binding: ItemMsgSendBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val doubt = mList[position]
            binding.tvMsg.text = doubt.comments
            binding.tvTime.text = doubt.created_date.getChatTime()

            binding.executePendingBindings()
        }
    }

    inner class ViewHolderSendImage(val binding: ItemMsgImageSendBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val doubt = mList[position]
            if (doubt.comments == "") {
                binding.tvMsg.visibility = View.GONE
            } else {
                binding.tvMsg.visibility = View.VISIBLE
            }
            binding.tvMsg.text = doubt.comments
            binding.tvTime.text = doubt.created_date.getChatTime()
            setDoubtImage(binding.ivImage, doubt.src_url)
            binding.root.setOnClickListener {
                val intent = Intent(mContext, WebViewActivity::class.java)
                    .putExtra(EXTRA_KEY_URL, doubt.src_url)
                    .putExtra(EXTRA_KEY_EXTENSION, FT_IMG)
                    .putExtra(EXTRA_KEY_TITLE, "")
                mContext.startActivity(intent)
            }
            binding.executePendingBindings()
        }
    }

    fun updateList(List: ArrayList<DoubtData>) {
        mList = List
        notifyDataSetChanged()
    }
}