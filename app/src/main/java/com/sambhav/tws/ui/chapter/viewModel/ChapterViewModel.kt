package com.sambhav.tws.ui.schedule.viewModel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.chapter.model.ChapterData
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService

class ChapterViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {

    val chapterList = MutableLiveData<ArrayList<ChapterData>>()

    fun getChapter(subjectId: String) =
        if (pref.isStudent) getStudentChapters(subjectId)
        else getTeacherChapter(subjectId)

    private fun getTeacherChapter(subjectId: String){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KE_METHOD, ALL_DATA)
        Log.e("ASDASDASDASDASD","onActivityStarted"+pref.gradeId)
        Log.e("ASDASDASDASDASD","onActivityStarted"+subjectId)
        requestData(mNetworkService.getAllTeacherChapters(pref.token,map),{
            chapterList.postValue(it.data)
        },{m,c->
            message.set(m)
        })
    }

    private fun getStudentChapters(subjectId: String){
        val map = JsonObject()
       // map.addProperty(API_KEY_SUBJECT_ID,"2")
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID, getStudentData(pref).grade_id)
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getAllStudentChapters(pref.token,map),{
            chapterList.postValue(it.data)
        },{m,c->
            message.set(m)
        })
    }

}