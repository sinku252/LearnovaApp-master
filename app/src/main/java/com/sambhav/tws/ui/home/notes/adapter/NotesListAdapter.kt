package com.sambhav.tws.ui.home.notes.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.base.BaseBindingAdapter
import com.sambhav.tws.base.BaseCallback
import com.sambhav.tws.base.BaseViewHolder
import com.sambhav.tws.databinding.ItemEventHeaderBinding
import com.sambhav.tws.databinding.ItemNotesListBinding
import com.sambhav.tws.databinding.ItemTeacherNotesListBinding
import com.sambhav.tws.ui.home.notes.model.NotesSubListModel
import com.sambhav.tws.ui.home.notes.service.NotesDownloadService
import com.sambhav.tws.ui.home.notes.service.ProgressListener
import com.sambhav.tws.utils.*
import getDeviceRatio
import isDocDownload

class NotesListAdapter(
    private val mContext: Context,
    var mList: ArrayList<NotesSubListModel>,
    val mCallback: BaseCallback
) : BaseBindingAdapter()
{
    var isStudent = true
    var mDeviceRatio = Pair(0, 0)

    init {
        mDeviceRatio = getDeviceRatio(mContext)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        if (mList[viewType].id == "") {
            return ViewHolderHeader(
                ItemEventHeaderBinding.inflate(
                    getInflater(parent), parent,
                    false
                )
            )
        } else {
            if(isStudent){
                return ViewHolderEvent(
                    ItemNotesListBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            }else{
                return ViewHolderTeacher(
                    ItemTeacherNotesListBinding.inflate(
                        getInflater(parent), parent,
                        false
                    )
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolderEvent(val binding: ItemNotesListBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val notes = mList[position]
            val dte = mList[position].created_date
            /*mList[position].created_date = dte.changeDate()
            mList[position].created_time = dte.changeTime()*/

            binding.tvDate.text=dte.changeDate()
            binding.tvTime.text=dte.changeTime()

            binding.sub = mList[position]
            binding.tvType.text = String.format(
                "%s - %s", mList[position].format_type.capitalize(),
                mList[position].title.capitalize()
            )

            binding.executePendingBindings()

            binding.progressBar.visibility =
                if (NotesDownloadService.containsTask(mList[position].id)) {
                    View.VISIBLE
                } else View.GONE

            binding.ivDownload.visibility =
                if (isDocDownload(notes)) View.GONE else View.VISIBLE

            binding.tvView.visibility =
                if (isDocDownload(notes)) View.VISIBLE else View.GONE

            binding.tvView.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })


            binding.ivDownload.setOnClickListener(CustomClickListener {
                addProgressListeners(binding, position)
                binding.progressBar.visibility = View.VISIBLE
                binding.ivCancel.visibility = View.VISIBLE
                binding.tvView.visibility = View.GONE
                binding.ivDownload.visibility = View.GONE

                mCallback.onItemClick(position, ACTION_DOWNLOAD)
            })

            binding.ivCancel.setOnClickListener(CustomClickListener {
                addProgressListeners(binding, position)
                binding.progressBar.visibility = View.GONE
                binding.ivCancel.visibility = View.GONE
                binding.tvView.visibility = View.GONE
                binding.ivDownload.visibility = View.VISIBLE

                NotesDownloadService.cancelTask(notes.id, notes.dwonLoadId)
            })
        }

        private fun addProgressListeners(
            binding: ItemNotesListBinding,
            position: Int
        ) {
            NotesDownloadService.addProgressListener(mList[position].id, object : ProgressListener {
                override fun onProgressUpdate(progress: Int) {
                    binding.progressBar.progress = progress
                }

                override fun onStart(id: Int) {
                    binding.progressBar.visibility = View.VISIBLE
                    mList[position].dwonLoadId = id
                }

                override fun onSuccess(path: String) {

                    binding.ivCancel.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.ivDownload.visibility = View.GONE
                    binding.tvView.visibility = View.VISIBLE
                }

                override fun onFileNotFound() {
                    if (mContext is BaseActivity)
                        mContext.toast("Downloading cancel file not foung!!")

                    binding.ivCancel.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.ivDownload.visibility = View.VISIBLE
                    binding.tvView.visibility = View.GONE
                }

                override fun onCancel() {
                    if (mContext is BaseActivity)
                        mContext.toast("Downloading failed Try - Again")

                    binding.ivCancel.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.ivDownload.visibility = View.VISIBLE
                    binding.tvView.visibility = View.GONE
                }
            })
        }
    }

    inner class ViewHolderTeacher(val binding: ItemTeacherNotesListBinding) : BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            val notes = mList[position]
            val dte = mList[position].created_date
            mList[position].created_date = dte.changeDate()
            mList[position].created_time = dte.changeTime()

            binding.sub = mList[position]
            binding.tvType.text = String.format(
                "%s - %s", mList[position].format_type.capitalize(),
                mList[position].title.capitalize()
            )

            binding.executePendingBindings()
            binding.layoutView.visibility = if (isStudent) View.GONE else View.VISIBLE

            binding.progressBar.visibility =
                if (NotesDownloadService.containsTask(mList[position].id)) {
                    View.VISIBLE
                } else View.GONE

            binding.ivDownload.visibility =
                if (isDocDownload(mList[position])) View.GONE else View.VISIBLE

            binding.tvViewTec.visibility =
                if (isDocDownload(mList[position])) View.VISIBLE else View.GONE


            binding.tvViewTec.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_VIEW)
            })

            binding.tvDel.setOnClickListener(CustomClickListener {
                mCallback.onItemClick(position, ACTION_DELETE)
            })

            binding.ivDownload.setOnClickListener(CustomClickListener {
                addProgressListeners(binding, position)
                binding.progressBar.secondaryProgress = 0
                binding.progressBar.visibility = View.VISIBLE
                binding.ivCancel.visibility = View.VISIBLE
                binding.tvViewTec.visibility = View.GONE
                binding.ivDownload.visibility = View.GONE
                mCallback.onItemClick(position, ACTION_DOWNLOAD)

            })

            binding.ivCancel.setOnClickListener(CustomClickListener {
                binding.progressBar.secondaryProgress = 0
                binding.progressBar.visibility = View.GONE
                binding.ivCancel.visibility = View.GONE
                binding.tvViewTec.visibility = View.GONE
                binding.ivDownload.visibility = View.VISIBLE
                NotesDownloadService.cancelTask(notes.id, notes.dwonLoadId)

            })


        }

        private fun addProgressListeners(
            binding: ItemTeacherNotesListBinding,
            position: Int
        ) {
            NotesDownloadService.addProgressListener(mList[position].id, object : ProgressListener {
                override fun onProgressUpdate(progress: Int) {
                    binding.progressBar.secondaryProgress = progress
                }

                override fun onStart(id: Int) {
                    binding.progressBar.visibility = View.VISIBLE
                    mList[position].dwonLoadId = id
                }

                override fun onSuccess(path: String) {

                    binding.progressBar.secondaryProgress = 0

                    binding.ivCancel.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.ivDownload.visibility = View.GONE
                    binding.tvViewTec.visibility = View.VISIBLE

                }

                override fun onFileNotFound() {
                    if (mContext is BaseActivity)
                        mContext.toast("Downloading cancel file not foung!!")
                    binding.progressBar.secondaryProgress = 0
                    binding.ivCancel.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.ivDownload.visibility = View.VISIBLE
                    binding.tvViewTec.visibility = View.GONE

                }

                override fun onCancel() {
                    if (mContext is BaseActivity)
                        mContext.toast("Downloading failed Try-again")
                    binding.progressBar.secondaryProgress = 0
                    binding.ivCancel.visibility = View.GONE
                    binding.progressBar.visibility = View.GONE
                    binding.ivDownload.visibility = View.VISIBLE
                    binding.tvViewTec.visibility = View.GONE
                }
            })
        }
    }

    inner class ViewHolderHeader(val binding: ItemEventHeaderBinding) :
        BaseViewHolder(binding.root) {
        override fun bindData(position: Int) {
            binding.tvHeader.text = mList[position].title.capitalize()
        }
    }

    fun updateList(List: ArrayList<NotesSubListModel>) {
        mList = List
        notifyDataSetChanged()
    }

}