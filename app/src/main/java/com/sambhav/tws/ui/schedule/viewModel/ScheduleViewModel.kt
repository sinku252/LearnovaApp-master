package com.sambhav.tws.ui.schedule.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.apiModel.LiveClassData
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class ScheduleViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {

    val liveClasses = MutableLiveData<ArrayList<LiveClassData>>()
    val createSchedule = MutableLiveData<String>()

    fun createSchedule(map: JsonObject){
        requestData(mNetworkService.createSchedule(pref.token,map),{
            createSchedule.postValue(it.message)
        },{m,c->
            createSchedule.postValue(m)
        })
    }

    fun getLiveClasses() {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,"2")
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.teacherLiveClasses(pref.token, map), {
            liveClasses.postValue(it.data)
        })
    }


    fun getStudentLiveClasses(subject:String) {
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KEY_STUDENT_ID,getStudentData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subject)
        map.addProperty(API_KEY_GRADE_ID,getStudentData(pref).grade_id)
        map.addProperty(API_KEY_METHOD,GET_ALL_DATA)
        requestData(mNetworkService.getAllStudentLiveClass(pref.token, map), {
            liveClasses.postValue(it.data)
        })
    }
}