package com.sambhav.tws.ui.home.classes.activities


import android.os.Bundle
import androidx.databinding.DataBindingUtil

import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityStudentLiveClassBinding
import com.sambhav.tws.ui.home.videos.adapter.OneToOneChatAdapter
import com.sambhav.tws.utils.CustomClickListener
import kotlinx.android.synthetic.main.activity_student_live_class.*
import kotlinx.android.synthetic.main.layout_chat_card.*

class StudentLiveClassActivity : BaseActivity() {
    private lateinit var mBinding:ActivityStudentLiveClassBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_student_live_class)
        recycler_view.adapter = OneToOneChatAdapter(this, ArrayList())

        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
    }
}
