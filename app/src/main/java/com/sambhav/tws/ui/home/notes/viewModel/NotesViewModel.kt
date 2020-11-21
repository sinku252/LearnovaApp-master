package com.sambhav.tws.ui.home.notes.viewModel

import androidx.lifecycle.MutableLiveData
import com.google.gson.JsonObject
import com.sambhav.tws.R
import com.sambhav.tws.base.BaseViewModel
import com.sambhav.tws.ui.chapter.model.ChapterData
import com.sambhav.tws.ui.home.notes.model.NotesSubListModel
import com.sambhav.tws.utils.*
import com.sambhav.tws.utils.networkRequest.ApiService
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class NotesViewModel(
    private val mNetworkService: ApiService,
    private val pref: SharedPreferencesHelper
) : BaseViewModel() {

    val uploadData = MutableLiveData<Boolean>()
    val uploadFileUrl = MutableLiveData<String>()
    val deleteData = MutableLiveData<Boolean>()
    val allNotes = MutableLiveData<ArrayList<NotesSubListModel>>()
    val allChapter = MutableLiveData<ArrayList<ChapterData>>()

    fun getNotes(subjectId: String,chapterId: String) =
        if (pref.isStudent) getStudentNotes(subjectId,chapterId)
        else getTeacherNotes(subjectId,chapterId)

    fun getTeacherNotes(subjectId: String,chapterId: String){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getAllTeacherNotes(pref.token, map), {
            if(it.data?.isEmpty() != false && !it.error){
                noIcon.set(R.drawable.ic_add_file)
                message.set(it.message)
            }else{
                allNotes.value = it.data
            }
        })
    }

    fun getStudentNotes(subjectId: String,chapterId: String){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getStudentData(pref).school_id)
        map.addProperty(API_KEY_STUDENT_ID,getStudentData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,getStudentData(pref).grade_id)
        map.addProperty(API_KEY_CHAPTER_ID,chapterId)
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getAllStudentNotes(pref.token, map), {
            if(it.data?.isEmpty() != false && !it.error){
                noIcon.set(R.drawable.ic_add_file)
                message.set(it.message)
            }else{
                allNotes.value = it.data
            }
        })
    }

    fun getTeacherChapters(subjectId : String){
        val map = JsonObject()
        map.addProperty(API_KE_SCHOOL_ID, getTeacherData(pref).school_id)
        map.addProperty(API_KE_TEACHER_ID,getTeacherData(pref).id)
        map.addProperty(API_KEY_SUBJECT_ID,subjectId)
        map.addProperty(API_KEY_GRADE_ID,pref.gradeId)
        map.addProperty(API_KE_METHOD, ALL_DATA)
        requestData(mNetworkService.getAllTeacherChapters(pref.token, map), {
            allChapter.postValue(it.data)
        })
    }

    fun uploadFile(file:File) {
        val requestFile: RequestBody = RequestBody.create(
            MediaType.parse("multipart/form-data"),
            file
        )
        val multipartBody =MultipartBody.Part.createFormData("documentImg", file.name, requestFile)
        requestData(mNetworkService.uploadData(pref.token,"document",multipartBody),{
            uploadFileUrl.postValue(it.data?.full_path)
        },{m,c->
            mErrorMessage.postValue(m)
        })
    }

    fun uploadNotes(map: JsonObject) {
        requestData(mNetworkService.uploadTeacherNotes(pref.token,map),{
            uploadData.postValue(true)
        },{m,c->
            uploadData.postValue(false)
        })
    }

    fun deleteNotes(id:String) {
        requestData(mNetworkService.deleteTeacherNotes(pref.token, id), {
            deleteData.postValue(true)
        },{m,c->
            deleteData.postValue(false)
        })
    }



}