package com.sambhav.tws.ui.home.doubt.model

data class MessageEvent(
    var msg : String
)

data class NotificationModel(
    var student_id : String="",
    var school_id : String="",
    var subject_id : String="",
    var subject_name : String="",
    var subject_icon : String="",
    var grade_id : String="",
    var sender_id : String="",
    var document_library_id : String="",
    var sender_type : String="",
    var body : String="",
    var attachment_type : String="",
    var type : String=""

)

