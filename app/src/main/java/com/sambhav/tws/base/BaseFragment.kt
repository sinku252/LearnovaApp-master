package com.sambhav.tws.base

import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.sambhav.tws.ui.grade.model.GradeData
import com.sambhav.tws.ui.login.model.TeacherModel
import com.sambhav.tws.ui.subject.model.SubjectData
import com.sambhav.tws.utils.SharedPreferencesHelper
import org.koin.android.ext.android.get

abstract class BaseFragment : Fragment(),BaseCallback {
    val mPreference: SharedPreferencesHelper = get()
    var mAllSub = getAllSub()

    val mTeacherData = Gson().fromJson(mPreference.teacherData, TeacherModel::class.java)

    fun getAllSub(): ArrayList<SubjectData> {
        val myType = object : TypeToken<List<SubjectData>>() {}.type
        val list = Gson().fromJson<List<SubjectData>>(mPreference.allSubject, myType)?:ArrayList()
        return list as ArrayList
    }
    fun getAllGrade(): ArrayList<GradeData> {
        val myType = object : TypeToken<List<GradeData>>() {}.type
        val list = Gson().fromJson<List<GradeData>>(mPreference.allGrade, myType)?:ArrayList()
        return list as ArrayList
    }
    override fun onItemClick(position: Int, action: String) {}
}
