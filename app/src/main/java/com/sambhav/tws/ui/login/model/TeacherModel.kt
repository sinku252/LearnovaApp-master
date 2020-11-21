package com.sambhav.tws.ui.login.model

data class TeacherModelResponse(
    val message: String,
    val error: Boolean,
    val data: TeacherModel? = TeacherModel()
)

data class TeacherModel(
    var id : String="",
    var password : String="",
    var first_name : String="",
    var last_name : String="",
    var username : String="",
    var school_id : String="",
    var email : String="",
    var mobile : String="",
    var gender : String="",
    var profile_image : String="",
    var is_login : Boolean=false,
    var app_token : String="",
    var grade_title : String="",
    var type : String="",
    var max_upload_size : String="",
    var status : String="",
    var user_ip : String="",
    var last_login_device : String="",
    var device_type : String="",
    var browser : String="",
    var browser_version : String="",
    var os : String="",
    var mobile_device : String="",
    var last_login_date : String="",
    var created_by : String="",
    var created_date : String="",
    var modified_date : String="",
    var techerInfo : ArrayList<TeacherInfo>? = ArrayList()
)

data class TeacherInfo(
    var grade_id : String="",
    var subject_id : String="",
    var grade_title : String="",
    var grade_image : String="",
    var subject_title : String="",
    var subject_image : String=""
)