package com.sambhav.tws.ui.home.exam.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.google.android.material.tabs.TabLayout
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityExamListBinding
import com.sambhav.tws.ui.home.classes.fragment.ClassFragment
import com.sambhav.tws.ui.home.exam.adapter.ExamListAdapter
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.ui.payemnt.BasePaymentActivity
import com.sambhav.tws.utils.*
import getAlertDailog
import kotlinx.android.synthetic.main.activity_exam_list.*
import kotlinx.android.synthetic.main.layout_back.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExamListActivity : BaseActivity()
{
    private val mViewModel: ExamViewModel by viewModel()
    private var mList = ArrayList<ExamData>()
    private lateinit var mBinding: ActivityExamListBinding
    private var adapter: ExamListAdapter?= null
    /*private var mSubjectId =""
    private var mSubjectName =""
    private var mChapterId =""
    private var mChapterName =""*/

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_exam_list)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        mViewModel.getTestList(0)
        init()
        fetchFromViewModel()
        handleAuth(mViewModel)
    }

    fun init() {
        mBinding.toolbar.tvTitle.text = "Test"
        mBinding.toolbar.tvSubTitle.text = "Series"

        rootLayout?.setBackgroundResource(R.drawable.back_video)

        adapter = ExamListAdapter(this, ArrayList(), this)
        rv_exam.adapter=adapter
        //recycler_view.adapter = adapter

        examback.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        mBinding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {

            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                getList(mBinding.tabs.selectedTabPosition)
            }


        })


    }

    fun getList(type:Int)
    {
        mViewModel.getTestList(type)
        fetchFromViewModel()
        Log.e("faf","safasf")

    }

    fun fetchFromViewModel(){
        mViewModel.attemptList.observe(this, Observer {
            mList.clear()
            mList.addAll(it)
            adapter?.updateList(mList)
        })

        mViewModel.unattemptList.observe(this, Observer {
            mList.clear()
            mList.addAll(it)
            adapter?.updateList(mList)
        })


       /* mViewModel.allTestList.observe(this, Observer {
            Log.e("ASDASDASDASDASD","onActivityStarted"+it)
            *//*     mList.clear()
                 mList.add(ChapterData(chapter_title = "Chapter"))
                 mList.addAll(it)
                 adapter?.updateList(mList)*//*
        })*/
    }

    override fun onItemClick(position: Int, action: String) {
        super.onItemClick(position, action)
        val tabPosition =  mBinding.tabs.selectedTabPosition
        if(tabPosition>0)
        {
            val examDataString:String=Gson().toJson(mList[position])
            val intent = Intent(this, ExamResultActivity::class.java)
            intent.putExtra(EXTRA_KEY_EXAM_DATA,examDataString);
            startActivity(intent)
        }
        else
        {
            if (mPreference.isStudent)
            {
                if (getStudentData(mPreference).is_premium == "0")
                {
                    startActivityForResult(Intent(this, BasePaymentActivity::class.java), 1000)
                   /*if(position==0)
                   {
                       val examDataString:String=Gson().toJson(mList[position])
                       startTest(examDataString)
                   }
                    else
                   {
                       startActivityForResult(Intent(this, BasePaymentActivity::class.java), 1000)
                   }*/
                }
                else
                {
                    val examDataString:String=Gson().toJson(mList[position])
                    startTest(examDataString)
                }
            }

        }

        overridePendingTransition(0,0)
    }


    override fun onResume() {
        super.onResume()
        getList(mBinding.tabs.selectedTabPosition)
        //mViewModel.getNotes(mSubjectId, mChapterId)
    }

    fun startTest(examDataString:String)
    {
        val intent = Intent(this, StartExamActivity::class.java)
     /*   intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubjectId)
        intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubjectName)*/
        intent.putExtra(EXTRA_KEY_EXAM_DATA,examDataString);
        startActivity(intent)

    }


}