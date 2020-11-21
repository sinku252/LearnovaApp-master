package com.sambhav.tws.ui.home.exam.model

data class QuestionsListResponse(
    var error:Boolean=false,
    var message:String="",
    var data: ArrayList<QuestionData> ?= ArrayList()
)

data class QuestionData(
    val  id: Int=0,
    val  grade_id: Int=0,
    val  subject_id: Int=0,
    val  chapter_id: Int=0,
    val  lecture_id: Int=0,
    val  topic_id: Int=0,
    val  question_title:  String="",
    val  short_description:  String="",
    val  question_img:  String="",
    val  question_type:  String="",
    val  level:  String="",
    val  option_1_text:  String="",
    val  option_2_text:  String="",
    val  option_3_text:  String="",
    val  option_4_text:  String="",
    val  option_5_text:  String="",
    val  right_answer:  String="",
    val  solution:  String="",
    val  created_date:  String="",
    val  modified_date:  String="",
    val  created_by:  Int=0,
    val  modified_by:  Int=0,
    val  created_by_type:  String="",

    //UIspecific
    var test_id:String="",
    var question_attempt:Boolean=false,
    var user_input:String="",
    var answer_later:Boolean=false,
    var radioButton: Int=0,
    var checkBox:String=""
)

