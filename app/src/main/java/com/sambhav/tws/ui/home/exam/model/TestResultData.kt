package com.sambhav.tws.ui.home.exam.model

import com.sambhav.tws.ui.login.model.TeacherModel

data class TestResultData(
    var error:Boolean=false,
    var message:String="",
    val data: TestResultModel? = TestResultModel()
)

data class TestResultModel(
    val total_correct:String="",
    val total_incorrect:String="",
    val total_attempt:String="",
    val total_unattempt:String="",
    val total_marks:String="0",
    val total_marks_gain:String="0",
    val total_positive_marks:String="",
    val total_negative_marks:String="",
    var attempQuestionsList: ArrayList<AttempQuestionsData> ?= ArrayList()
)

data class AttempQuestionsData(
    val  id: Int=0,
    val  attempt_id: Int=0,
    val  question_id: Int=0,
    val  answer:  String="",
    val  is_correct:  String="",
    val  question_title:  String="",
    val  short_description:  String="",
    val  question_type:  String="",
    val  option_1_text:  String="",
    val  option_2_text:  String="",
    val  option_3_text:  String="",
    val  option_4_text:  String="",
    val  option_5_text:  String="",
    val  created_date:  String="",
    val  question_img:  String="",
    val  solution:  String=""

)