package com.sambhav.tws.ui.home.home.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.home.home.model.Notification
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class HomeViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
): BaseViewModel(){

    val allNotification = MutableLiveData<ArrayList<Notification>>()

    init {
        getNotification()
    }

    fun getNotification() =
        if (pref.isStudent) getStudentNotification()
        else getTeacherNotification()

    private fun getStudentNotification(){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KEY_STUDENT_ID,getStudentData(pref).id) //10
        map.addProperty(API_KEY_GRADE_ID,getStudentData(pref).grade_id)
        map.addProperty(API_KEY_USER_TYPE,"student")
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getNotification(pref.token, map), {
            if(it.data?.isEmpty() != false && !it.error){
                message.set(it.message)
            }else{
                allNotification.value = it.data
            }
        })
    }

    private fun getTeacherNotification(){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(pref).id) //10
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_USER_TYPE,"teacher")
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getNotification(pref.token, map), {
            if(it.data?.isEmpty() != false && !it.error){
                message.set(it.message)
            }else{
                allNotification.value = it.data
            }
        })
    }

}