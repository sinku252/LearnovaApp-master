package com.sambhav.tws.ui.home.doubt.model

import com.sambhav.tws.utils.DUMMY_URL

data class DoubtRoomModel(
    var name:String="Student",
    var profile_image:String ?= DUMMY_URL,
    var commenter_id:String= "",
    var document_library_id:String= "",
    var created_by:String= "",
    var student_name:String= "",
    var date:String = "",
    var total_doubts:String= "0"
)