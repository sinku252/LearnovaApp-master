package com.sambhav.tws.apiModel
data class LiveClassResponse(
    var error:Boolean=false,
    var message:String="",
    var data: ArrayList<LiveClassData> ?= ArrayList()
)

data class LiveClassData(
    var id:String="",
    var subject_id:String="",
    var subject_title:String="",
    var teacher_id:String="",
    var subject_image:String="",
    var teacher_first_name:String="",
    var teacher_last_name:String="",
    var teacher_image:String="",
    var schedule_date:String ?= "",
    var schedule_time:String="",
    var date_header:String="",
    var teacherName:String = teacher_first_name
)

data class CreateScheduleResponse(
    var error:Boolean=false,
    var message:String=""
)
