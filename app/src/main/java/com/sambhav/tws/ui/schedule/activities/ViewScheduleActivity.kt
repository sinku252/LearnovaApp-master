package com.sambhav.tws.ui.schedule.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.sambhav.tws.R
import com.sambhav.tws.apiModel.LiveClassData
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.base.PermissionData
import com.sambhav.tws.databinding.ActivityViewScheduleBinding
import com.sambhav.tws.ui.home.classes.activities.StudentLiveClassActivity
import com.sambhav.tws.ui.home.classes.adapter.LiveClassAdapter
import com.sambhav.tws.ui.schedule.viewModel.ScheduleViewModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.itemdecoration.CustomItemVerDecoration
import getDate
import getEvent
import getScheduleTime
import kotlinx.android.synthetic.main.activity_view_schedule.*
import kotlinx.android.synthetic.main.layout_back.*
import kotlinx.android.synthetic.main.layout_toolbar.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ViewScheduleActivity : BaseActivity() {

    private val mViewModel: ScheduleViewModel by viewModel()
    private lateinit var mBinding: ActivityViewScheduleBinding
    private var mList = ArrayList<LiveClassData>()
    private var adapter: LiveClassAdapter? = null
    private var mSubjectId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this, R.layout.activity_view_schedule)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        rootLayout.setBackgroundResource(R.drawable.back_class)
        init()
        getDataFromViewModel()
        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })

        if (mPreference.isStudent) {
            mViewModel.getStudentLiveClasses("")
        } else {
            mViewModel.getLiveClasses()
        }
    }

    fun getDataFromViewModel() {
        mViewModel.liveClasses.observe(this, Observer {
            mList.clear()
            setHeader(it)
            adapter?.updateList(mList)
        })
    }

    private fun init() {

        if (intent.getBooleanExtra(EXTRA_KEY_IS_START, false)) {
            toolbar.tvTitle.text = "Start"
            toolbar.tvSubTitle.text = "A class"
        } else {
            toolbar.tvTitle.text = "View"
            toolbar.tvSubTitle.text = "Schedule"
        }

        recycler_view.addItemDecoration(
            CustomItemVerDecoration(
                resources.getDimension(R.dimen.margin18dp).toInt()
            )
        )
        adapter = LiveClassAdapter(this, ArrayList(), this)

        adapter?.isStudent = mPreference.isStudent
        adapter?.isStart = intent.getBooleanExtra(EXTRA_KEY_IS_START, false)
        recycler_view.adapter = adapter
        handleAuth(mViewModel)

    }

    fun setHeader(list: ArrayList<LiveClassData>) {
        mList.clear()
        for (i in list.indices) {
            val classData = list[i]
            classData.schedule_date = classData.schedule_date.getDate()
            classData.schedule_time = classData.schedule_time.getScheduleTime()
            classData.date_header =
                (classData.schedule_date + " " + classData.schedule_time).getEvent()
        }

        val map: LinkedHashMap<String, ArrayList<LiveClassData>> =
            list.groupBy { it.date_header } as LinkedHashMap<String, ArrayList<LiveClassData>>

        val todayList = map.get(TODAY_EVENT) ?: ArrayList()
        val UpList = map.get(UPCOMIG_EVENT) ?: ArrayList()
        val pastList = map.get(PAST_EVENT) ?: ArrayList()

        if (todayList.isNotEmpty()) {
            mList.add(LiveClassData(schedule_date = TODAY_EVENT))
            mList.addAll(todayList)
        }
        if (UpList.isNotEmpty()) {
            mList.add(LiveClassData(schedule_date = UPCOMIG_EVENT))
            mList.addAll(UpList)
        }
        if (pastList.isNotEmpty()) {
            mList.add(LiveClassData(schedule_date = PAST_EVENT))
            mList.addAll(pastList)
        }
        Log.d("forEach", " " + mList.size)

    }

    override fun onItemClick(position: Int, action: String) {
        when (action) {
            ACTION_VIEW -> {
                if (mPreference.isStudent) {
                    val intent = Intent(this, StudentLiveClassActivity::class.java)
                    intent.putExtra(EXTRA_KEY_ACTION, ACTION_START)
                    startActivity(intent)
                    this.overridePendingTransition(0, 0)
                }
            }

            ACTION_START -> {
                PermissionData(this).showPermissionNeededDialog(
                    arrayOf(
                        android.Manifest.permission.CAMERA,
                        android.Manifest.permission.RECORD_AUDIO
                    )
                ) {
                    /*if (it) {
                        val classtime = getClassTime( mList[position].schedule_date, mList[position].schedule_time)
                        val url = "https://kotafactory.learnoma.in/webview/teacher/live-class?startTeacher=1&time=$classtime"
                        val intent = Intent(
                            this@ViewScheduleActivity, LiveActivity::class.java
                        )
                        intent.putExtra(EXTRA_KEY_SUBJECT_NAME, mList[position].subject_title)
                        intent.putExtra("icon", mList[position].subject_image)
                        intent.putExtra(EXTRA_KEY_URL, url)
                        startActivity(intent)
                    }*/
                }
                /*this.getFullScreenDialog(R.layout.dialog_class).apply {
                    show()
                    tvTitle.text = "Start a Class"
                    tvSubTitle.text = "It's still time as per schedule choose."
                    btn_submit.text = "Start Anyway"
                    btn_notSubmit.text = "Waiting Room"
                    val intent = Intent(this@ViewScheduleActivity, LiveActivity::class.java)

                    btn_submit.setOnClickListener(DebounceClickListener {
                        intent.putExtra(EXTRA_KEY_ACTION, ACTION_START)
                        startActivity(intent)
                        this@ViewScheduleActivity.overridePendingTransition(0,0)

                        dismiss()
                    })

                    btn_notSubmit.setOnClickListener(DebounceClickListener {
                        intent.putExtra(EXTRA_KEY_ACTION, ACTION_WAIT)
                        startActivity(intent)
                        this@ViewScheduleActivity.overridePendingTransition(0,0)
                        dismiss()
                    })
                }*/
            }

            ACTION_EDIT -> {
                val intent = Intent(this@ViewScheduleActivity, ScheduleActivity::class.java)
                intent.putExtra(EXTRA_KEY_ACTION, ACTION_EDIT)
                startActivity(intent)
            }
        }

    }
}
