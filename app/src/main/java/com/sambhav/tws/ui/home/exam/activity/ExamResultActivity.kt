package com.sambhav.tws.ui.home.exam.activity

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityExamListBinding
import com.sambhav.tws.databinding.ActivityExamResultBinding
import com.sambhav.tws.databinding.ActivityTeacherVideosListBinding
import com.sambhav.tws.ui.home.exam.adapter.ExamListAdapter
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.utils.*
import getAlertDailog
import kotlinx.android.synthetic.main.activity_exam_list.*
import kotlinx.android.synthetic.main.activity_exam_result.*
import kotlinx.android.synthetic.main.layout_back.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExamResultActivity : BaseActivity()
{
    private val mViewModel: ExamViewModel by viewModel()
    private lateinit var mBinding: ActivityExamResultBinding

    /*private var mSubjectId =""
    private var mSubjectName =""*/
    private lateinit var examData:ExamData

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_exam_result)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

      /*  mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME)?:""*/
        val examDataString= intent.getStringExtra(EXTRA_KEY_EXAM_DATA)?:""
        examData = Gson().fromJson(examDataString, ExamData::class.java)
        mBinding.examData=examData


        mViewModel.getTestResult(examData?.id.toString())

        fetchFromViewModel()
        handleAuth(mViewModel)

        mBinding.llBack.setOnClickListener(CustomClickListener {
         finish()
        })

        mBinding.tvExamReport.setOnClickListener(CustomClickListener
        {
            val resultDataString:String=Gson().toJson(mBinding.resultData)
            val intent = Intent(this, SolutionActivity::class.java)
            intent.putExtra(EXTRA_KEY_EXAM_DATA, examDataString);
            intent.putExtra(EXTRA_KEY_RESULT_DATA, resultDataString);
            startActivity(intent)
        })

    }

    fun fetchFromViewModel(){
        mViewModel.testResult.observe(this, Observer {
            mBinding.resultData= it.data
            performance_progress_bar.max=mBinding.resultData!!.total_marks.toInt()
            performance_progress_bar.setProgress(mBinding.resultData!!.total_marks_gain.toInt())
          //  tv_marks.text=it.data?.total_marks+"\\"+"150"
           /* mList.clear()
            mList.addAll(it)
            adapter?.updateList(mList)*/
        })

    }
}