package com.sambhav.tws.ui.schedule.activities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseActivity
import com.sambhav.tws.databinding.ActivityEditScheduleBinding
import com.sambhav.tws.databinding.ItemWeekNameBinding
import com.sambhav.tws.ui.schedule.model.WeekModel
import com.sambhav.tws.ui.schedule.viewModel.ScheduleViewModel
import com.sambhav.tws.ui.subject.adapter.SubListAdapter
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.activity_edit_schedule.*
import kotlinx.android.synthetic.main.layout_login_header.*
import kotlinx.android.synthetic.main.layout_spinner.view.*
import org.koin.android.ext.android.get
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ScheduleActivity : BaseActivity() {

    private lateinit var mBinding: ActivityEditScheduleBinding
    private var mViewModel: ScheduleViewModel = get()
    private var mWeekList = ArrayList<WeekModel>()
    private var mAction = ACTION_EDIT
    private var mSubjectId =""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_edit_schedule)
        mBinding.viewModel = mViewModel
        mBinding.executePendingBindings()
        mAction = intent.getStringExtra(EXTRA_KEY_ACTION) ?: ACTION_EDIT
        setUserImageRound(mProfileImage, R.drawable.ic_deactivate_notes_resource)

        if (mAction == ACTION_EDIT) {
            tvTitle.text = getString(R.string.edit_schedule_title)
            tvTitleDes.visibility = View.GONE
        } else {
            tvTitle.text = getString(R.string.create_schedule)
            tvTitleDes.visibility = View.GONE
            btnSubmit.text = getString(R.string.publish)
        }

        mSubjectId = intent.getStringExtra(EXTRA_KEY_SUBJECT_ID)?:""

        initView()
        handleAuth(mViewModel)

    }

    private fun initView() {

        mWeekList.addAll(getWeekDays())

        addWeekItem()

        subject.spinner.adapter = SubListAdapter(applicationContext,mAllSub)
        subject.spinner.setSelection(getSubjectPosition(mAllSub))
        subject.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mSubjectId = getAllSub()[position].subject_id
            }
        }

        occurance.spinner.adapter = SubListAdapter(applicationContext, getOccurance())
        occurance.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (position == 0) {
                    layCustomDay.visibility = View.GONE
                } else {
                    layCustomDay.visibility = View.VISIBLE
                }
            }
        }

        back.setOnClickListener(CustomClickListener {
            onBackPressed()
        })
        btnSubmit.setOnClickListener(CustomClickListener {
            //startActivity(Intent(this, LiveActivity::class.java))
            if (checkValidation()) {
                createSchedule()
            }
        })

        etDate.setOnClickListener(CustomClickListener {
            showDateDialog()
        })

        etTime.setOnClickListener(CustomClickListener {
            showTimeDialog()
        })
    }

    private fun addWeekItem() {
        chipChopGroup.removeAllViews()
        val inflater = LayoutInflater.from(this)
        mWeekList[0].select = true
        mWeekList.forEach {
            val binding = ItemWeekNameBinding.inflate(inflater,chipChopGroup,false)
            binding.week = it
            chipChopGroup.addView(binding.root)
            binding.executePendingBindings()
        }
    }

    private fun showDateDialog() {
        val myCalendar = Calendar.getInstance();
        val dialog = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { p0, p1, p2, p3 ->
                myCalendar.set(Calendar.YEAR, p1)
                myCalendar.set(Calendar.MONTH, p2)
                myCalendar.set(Calendar.DAY_OF_MONTH, p3)

                val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val sDate = df.format(myCalendar.time)
                etDate.setText(sDate)
            },
            myCalendar.get(Calendar.YEAR),
            myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.datePicker.minDate = System.currentTimeMillis()
        dialog.show()
    }

    private fun showTimeDialog() {
        val myCalendar = Calendar.getInstance();
        val dialog = TimePickerDialog(
            this,
            3,
            TimePickerDialog.OnTimeSetListener { p0, p1, p2 ->
                myCalendar.set(Calendar.HOUR_OF_DAY, p1)
                myCalendar.set(Calendar.MINUTE, p2)
                //val df = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
                val df = SimpleDateFormat("hh:mm", Locale.getDefault())
                val sDate = df.format(myCalendar.time)
                etTime.setText(sDate)
            },
            myCalendar.get(Calendar.HOUR_OF_DAY),
            myCalendar.get(Calendar.MINUTE),
            false
        )
        dialog.show()
    }

    private fun checkValidation() : Boolean{
        if (occurance.spinner.selectedItemPosition == 1){
            val list = mWeekList.filter { it.select }
            Log.d("btnSubmit"," "+list.size)
            if (list.size < 1){
                toast("Select weekdays")
                return false
            }
        }
        if(etDate.text.isNullOrBlank()){
            toast("Enter date")
            return false
        }
        if(etTime.text.isNullOrBlank()){
            toast("Enter time")
            return false
        }
        return true
    }

    private fun getSubjectPosition(arrayList: ArrayList<SubjectData>) : Int{
        arrayList.forEachIndexed { index, subjectModel ->
            if (mSubjectId == subjectModel.subject_title)
                return index
        }
        return 0
    }

    private fun createSchedule(){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(mPreference).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(mPreference).id)
        map.addProperty(API_KEY_GRADE_ID,mPreference.gradeId)
        map.addProperty(API_KEY_SUBJECT_ID,mSubjectId)
        map.addProperty(API_KEY_CLASS_TYPE, "live-class")
        map.addProperty(API_KEY_REPEATS, getRepeatKey())
        //One time  //Recurring
        map.addProperty(API_KEY_SCHEDULE_TYPE, if (occurance.spinner.selectedItemPosition == 0)
            "schedule_time"
        else
            "recurring")
        map.addProperty(API_KEY_SCHEDULE_DATE, etDate.text.toString())
        map.addProperty(API_KEY_SCHEDULE_TIME, etTime.text.toString())
        map.addProperty(API_KEY_METHOD, POST_DATA)
        mViewModel.createSchedule(map)
        mViewModel.createSchedule.observe(this, androidx.lifecycle.Observer {
            Toast.makeText(this,it, Toast.LENGTH_SHORT).show()
            finish()
        })
    }

    private fun getRepeatKey():String{
        var mComaString = ""
        return if (occurance.spinner.selectedItemPosition == 0){
            ""
        }else{
            val mSelected = mWeekList.filter { it.select }
            mSelected.forEach {
                mComaString= mComaString+it.value+ ","
            }
            mComaString.subSequence(0,(mComaString.length-1)).toString()
        }
    }

}
