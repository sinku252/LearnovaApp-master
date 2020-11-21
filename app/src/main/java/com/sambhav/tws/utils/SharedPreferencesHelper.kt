package com.sambhav.tws.utils

import android.content.SharedPreferences

class SharedPreferencesHelper
internal constructor(val mPrefs: SharedPreferences) {

    fun clear(){
        val studentWelcomeHold = studentWelcome
        val teacherWelcomeHold = teacherWelcome
        mPrefs.edit().clear().apply()
        studentWelcome =  studentWelcomeHold
        teacherWelcome =  teacherWelcomeHold
    }

    var studentWelcome: Boolean
        get() = mPrefs.getBoolean("studentWelcome", false)
        set(value) = mPrefs.edit().putBoolean("studentWelcome", value).apply()

    var teacherWelcome: Boolean
        get() = mPrefs.getBoolean("teacherWelcome", false)
        set(value) = mPrefs.edit().putBoolean("teacherWelcome", value).apply()


    var isLogin: Boolean
        get() = mPrefs.getBoolean("isLogin", false)
        set(value) = mPrefs.edit().putBoolean("isLogin", value).apply()

    var isStudent: Boolean
        get() = mPrefs.getBoolean("isStudent", true)
        set(value) = mPrefs.edit().putBoolean("isStudent", value).apply()

    var token: String
        get() = mPrefs.getString("token", "")?:""
        set(value) = mPrefs.edit().putString("token", value).apply()

    var id: String
        get() = mPrefs.getString("id", "")?:""
        set(value) = mPrefs.edit().putString("id", value).apply()

    var studentData: String
        get() = mPrefs.getString("studentData", "{}")?:"{}"
        set(value) = mPrefs.edit().putString("studentData", value).apply()


    var teacherData: String
        get() = mPrefs.getString("teacherData", "{}")?:"{}"
        set(value) = mPrefs.edit().putString("teacherData", value).apply()

    var allSubject: String
        get() = mPrefs.getString("allSubject", "[]")?:"[]"
        set(value) = mPrefs.edit().putString("allSubject", value).apply()

    var allGrade: String
        get() = mPrefs.getString("allGrade", "[]")?:"[]"
        set(value) = mPrefs.edit().putString("allGrade", value).apply()

    var gradeId: String
        get() = mPrefs.getString("gradeId", "")?:""
        set(value) = mPrefs.edit().putString("gradeId", value).apply()

    var firebaseToken: String
        get() = mPrefs.getString("firebaseToken", "")?:""
        set(value) = mPrefs.edit().putString("firebaseToken", value).apply()

    var examData: String
        get() = mPrefs.getString("examData", "{}")?:"{}"
        set(value) = mPrefs.edit().putString("examData", value).apply()

}