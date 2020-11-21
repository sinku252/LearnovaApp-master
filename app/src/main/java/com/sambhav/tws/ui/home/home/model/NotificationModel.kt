package com.sambhav.tws.ui.home.home.model

data class NotificationResponseModel(
    var error: Boolean=false,
    var message: String ="",
    var data: ArrayList<Notification>? = ArrayList()
)

data class Notification(
    var id:String="",
    var status:String="",
    var title:String="",
    var priority:String="",
    var table_id:String="",
    var message:String="",
    var receiver_id:String="",
    var created_by:String="",
    var created_date:String="",
    var chapter_title:String="",
    var teacher_name:String=""
)