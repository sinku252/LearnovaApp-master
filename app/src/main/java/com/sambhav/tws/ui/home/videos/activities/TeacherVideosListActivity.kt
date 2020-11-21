package com.sambhav.tws.ui.home.videos.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.VideoData

import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityTeacherVideosListBinding
import com.sambhav.tws.ui.home.videos.adapter.VideosListAdapter
import com.sambhav.tws.ui.home.videos.viewModel.VideoViewModel
import com.sambhav.tws.ui.subject.adapter.SubListAdapter
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_teacher_videos_list.*
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.layout_spinner.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import androidx.recyclerview.widget.GridLayoutManager
import compareDate
import getAlertDailog

class TeacherVideosListActivity : BaseActivity() {
    private val mViewModel: VideoViewModel by viewModel()
    private var mList = ArrayList<VideoData>()
    private lateinit var mBinding:ActivityTeacherVideosListBinding
    private var mSubjectId =""
    private var mSubjectName =""
    private var mChapterId =""
    private var mChapterName =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_videos_list)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME)?:""
        mChapterId = intent.getStringExtra(EXTRA_KEY_CHAPTER_ID)?:""
        mChapterName = intent.getStringExtra(EXTRA_KEY_CHAPTER_NAME)?:""
        init()
        initClick()
        fetchFromViewModel()
        handleAuth(mViewModel)

    }

    fun fetchFromViewModel(){
        mViewModel.deleteData.observe(this, Observer {
            if(it){
               toast("Video deleted")
                mViewModel.getVideo(mSubjectId,mChapterId)
            }else{
                toast("Unable delete this video try again")
            }
        })
        mViewModel.allVideos.observe(this, Observer {
            setHeader(it)
            val manager = GridLayoutManager(this,2)
            manager.setSpanSizeLookup(object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (mList[position].id ==0) manager.spanCount else 1
                }
            })
            recycler_view.layoutManager = manager
            val adapter =  VideosListAdapter(this, mList,this)
            adapter.isStudent = mPreference.isStudent
            recycler_view.adapter =adapter
        })
    }

    private fun initClick() {
        spinner?.onItemSelectedListener = object :AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                mSubjectId = mAllSub[p2].subject_id
                mViewModel.getVideo(mSubjectId,mChapterId)
            }

        }
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        btnAdd.setOnClickListener(CustomClickListener {
            val intent = Intent(this, AddVideoActivity::class.java)
            intent.putExtra(EXTRA_KEY_SUBJECT_ID, mSubjectId)
            startActivity(intent)
        })
    }

    fun init() {
        mBinding.toolbar.tvTitle.text = "Video &"
        mBinding.toolbar.tvSubTitle.text = "Library"

        rootLayout?.setBackgroundResource(R.drawable.back_video)
        btnAdd.visibility = if (mPreference.isStudent) View.GONE else View.VISIBLE

        spinner?.adapter = SubListAdapter(this,mAllSub )
        val pos = mAllSub.indexOfFirst { it.subject_id == mSubjectId }
        if(pos > 0){
            spinner?.setSelection(pos)
        }
    }

    override fun onItemClick(position: Int, action: String) {
        when(action){
            ACTION_VIEW->{
                val intent = Intent(this, LiveVideoActivity::class.java)
                intent.putExtra(EXTRA_KEY_URL,mList[position].src_url)
                intent.putExtra(EXTRA_KEY_VIDEO_TYPE,mList[position].format_type)
                intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubjectId)
                intent.putExtra(EXTRA_KEY_CHAPTER_ID,mChapterId)
                intent.putExtra(EXTRA_KEY_VIDEO_ID,mList[position].id.toString())
                startActivity(intent)
                overridePendingTransition(0,0)
            }
            ACTION_DELETE->{
                this.getAlertDailog("","Are you sure you want to delete this video?", {
                    mViewModel.deleteVideo(mList[position].id.toString())
                    mViewModel.getVideo(mSubjectId,mChapterId)
                })
            }
        }
    }

    fun setHeader(list: ArrayList<VideoData>){
        mList.clear()
       for (i in list.indices){
           val header = VideoData(title= list[i].created_date.compareDate())
           /*if(i==0){
               //mList.add(0,header )
               Log.e("ASDASDASDASDASD",mSubjectName+"   "+mChapterName)
               mList.add(VideoData(title = String.format("%s - %s",mSubjectName,mChapterName)))
           }else{
               val date = list[i].created_date.compareDate()
               val prev = list[i-1].created_date.compareDate()
               Log.d("setHeader","date $date prev $prev")
               if(date != prev){
                   mList.add(header)
               }
           }*/
       }
        mList.add(VideoData(title = String.format("%s - %s",mSubjectName,mChapterName)))
        mList.addAll(list)
    }

    override fun onResume() {
        super.onResume()
        mViewModel.getVideo(mSubjectId,mChapterId)
    }
}
