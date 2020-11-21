package com.sambhav.tws.ui.home.exam.model

data class ExamListResponse(
    var error:Boolean=false,
    var message:String="",
    var data: ExamList
)
data class ExamList(var attempt_list: ArrayList<ExamData> ?= ArrayList(),
                    var unattempt_list: ArrayList<ExamData> ?= ArrayList())

data class ExamData(
    val id: Int=0,
    val grade_id: Int=0,
    val subject_id: Int=0,
    val lecture_id: Int=0,
    val chapter_id: Int=0,
    val topic_id: Int=0,
    val title: String="",
    val short_description: String="",
    val test_type: String="",
    val test_date: String="",
    val total_question: Int=0,
    val duration: Int=0,
    val level: String="",
    val status: Int=0,
    val created_date: String="",
    val modified_date: String="",
    val created_by: Int=0,
    val created_by_type: String="",
    val modified_by: Int=0,
    val subject_title: String="",
    val grade_title: String="",
    val attempt_id: String="",
    val test_question_count: Int=0


)

