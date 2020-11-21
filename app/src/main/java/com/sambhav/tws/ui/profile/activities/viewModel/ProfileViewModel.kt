package com.sambhav.tws.ui.profile.activities.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.login.model.StudentModel
import com.sambhav.tws.ui.login.model.StudentModelResponse
import com.sambhav.tws.ui.login.model.TeacherModel
import com.sambhav.tws.ui.login.model.TeacherModelResponse
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class ProfileViewModel (
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel(){

    init {
        if (pref.isStudent)
            getStudent()
        else
            getTeacher()
    }

    val teacherUpdate = MutableLiveData<TeacherModelResponse>()
    val studentUpdate = MutableLiveData<StudentModelResponse>()
    val teacherModel = MutableLiveData<TeacherModel>()
    val studentModel = MutableLiveData<StudentModel>()

    private fun getStudent(){
        val map = JsonObject()
        map.addProperty(API_KEY_ID, getStudentData(pref).id)
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KE_METHOD, SINGLE_DATA)
        requestData(mNetworkService.getStudent(pref.token,map),{
            studentModel.postValue(it.data)
        })
    }

    private fun getTeacher(){
        val map = JsonObject()
        map.addProperty(API_KEY_ID, getTeacherData(pref).id)
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_METHOD, SINGLE_DATA)
        requestData(mNetworkService.getTeacher(pref.token,map),{
            teacherModel.postValue(it.data)
        })
    }

    fun updateTeacher(map: JsonObject){
        map.addProperty(API_KEY_ID, getTeacherData(pref).id)
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_METHOD, POST_DATA)
        requestData(mNetworkService.updateTeacher(pref.token,map),{
            teacherUpdate.postValue(it)
        })
    }

    fun updateStudent(map: JsonObject){
        map.addProperty(API_KEY_ID, getStudentData(pref).id)
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KE_METHOD, POST_DATA)
        requestData(mNetworkService.updateStudent(pref.token,map),{
            studentUpdate.postValue(it)
        })
    }
}