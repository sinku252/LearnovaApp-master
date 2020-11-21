package com.sambhav.tws.ui.home.videos.activities


import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.sambhav.tws.R

import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityStudentVideosListBinding
import com.sambhav.tws.ui.home.videos.fragment.VideosFragment
import com.sambhav.tws.ui.home.videos.adapter.VideosDataAdapter
import com.sambhav.tws.ui.home.videos.model.CommonDataModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_student_videos_list.*
import kotlinx.android.synthetic.main.layout_back.*

/**
 * A simple [Fragment] subclass.
 */
class StudentVideoListActivity : BaseActivity() {
    private var mParent: VideosFragment? = null
    private lateinit var mBinding: ActivityStudentVideosListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_student_videos_list)
        mBinding.toolbar.tvTitle.text = "Video &"
        mBinding.toolbar.tvSubTitle.text = "Library"
        rootLayout?.setBackgroundResource(R.drawable.back_video)
        init()
        mBinding.executePendingBindings()

    }

    fun init() {
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        val list = ArrayList<CommonDataModel>()
        list.add(CommonDataModel("Today", getVideosList()))
        list.add(CommonDataModel("Previous", getVideosList()))

        VideosDataAdapter(this, list,this).apply {
            isStudent = mPreference.isStudent
            recycler_view.adapter =this
        }
    }

    override fun onItemClick(position: Int, action: String) {
        when(action){
            ACTION_VIEW ->{
                val intent = Intent(this, LiveVideoActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0,0)
            }
            ACTION_DELETE ->{

            }
        }
    }
}
