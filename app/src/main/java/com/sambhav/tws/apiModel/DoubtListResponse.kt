package com.sambhav.tws.apiModel

data class DoubtListResponse<T>(
    var message: String = "",
    var error: Boolean = false,
    var data: T
)

data class DoubtData(
    var id: String = "",
    var school_id: String = "",
    var teacher_id: String = "",
    var grade_id: String = "",
    var document_library_id: String = "",
    var subject_id: String = "",
    var commenter_id: String = "",
    var commenter_type: String = "",
    var comments: String = "",
    var status: String = "",
    var created_by: String = "",
    var src_url: String = "",
    var attachment_type: String = "",
    var created_date: String = "",
    var modified_date: String = "",
    var subject_title: String = ""

)