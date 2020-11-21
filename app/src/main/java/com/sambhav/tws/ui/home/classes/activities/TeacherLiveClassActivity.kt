package com.sambhav.tws.ui.home.classes.activities


import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil

import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityTeacherLiveClassBinding
import com.sambhav.tws.ui.home.videos.adapter.OneToOneChatAdapter
import com.sambhav.tws.utils.ACTION_START
import com.sambhav.tws.utils.CustomClickListener
import com.sambhav.tws.utils.EXTRA_KEY_ACTION
import kotlinx.android.synthetic.main.activity_teacher_live_class.*
import kotlinx.android.synthetic.main.layout_chat_card.*
import kotlinx.android.synthetic.main.layout_live_card.*

class TeacherLiveClassActivity : BaseActivity() {
    private lateinit var mBinding: ActivityTeacherLiveClassBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_teacher_live_class)

        val act = intent.getStringExtra(EXTRA_KEY_ACTION)?:""
        if(act == ACTION_START){
            frameLiveHeader?.visibility = View.VISIBLE
            recycler_view.adapter = OneToOneChatAdapter(this, ArrayList())
        }else{
            ivVideo?.visibility = View.GONE
            controller?.visibility = View.GONE
        }

        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
    }
}
