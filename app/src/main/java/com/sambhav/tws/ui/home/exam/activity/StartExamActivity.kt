package com.sambhav.tws.ui.home.exam.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityExamResultBinding
import com.sambhav.tws.databinding.ActivityStartExamBinding
import com.sambhav.tws.ui.home.exam.model.ExamData
import com.sambhav.tws.ui.home.exam.viewModel.ExamViewModel
import com.sambhav.tws.utils.CustomClickListener
import com.sambhav.tws.utils.EXTRA_KEY_EXAM_DATA
import com.sambhav.tws.utils.EXTRA_KEY_SUBJECT_ID
import com.sambhav.tws.utils.EXTRA_KEY_SUBJECT_NAME
import kotlinx.android.synthetic.main.activity_chapter.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class StartExamActivity :  BaseActivity() {
    private val mViewModel: ExamViewModel by viewModel()
    private lateinit var mBinding: ActivityStartExamBinding

    /*private var mSubjectId =""
    private var mSubjectName =""*/
    private var examData:ExamData?=null

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_start_exam)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()

        /*mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""
        mSubjectName = intent.getStringExtra(EXTRA_KEY_SUBJECT_NAME)?:""*/
        val examDataString= intent.getStringExtra(EXTRA_KEY_EXAM_DATA)?:""
        examData = Gson().fromJson(examDataString, ExamData::class.java)
        mBinding.examData=examData

        mBinding.back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        mBinding.llStartTest.setOnClickListener(CustomClickListener {
            val intent = Intent(this, TakeExamActivity::class.java)
            /*intent.putExtra(EXTRA_KEY_SUBJECT_ID,mSubjectId)
            intent.putExtra(EXTRA_KEY_SUBJECT_NAME,mSubjectName)*/
            intent.putExtra(EXTRA_KEY_EXAM_DATA,examDataString);
            startActivity(intent)
            finish()
        })


    }
}