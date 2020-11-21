package com.sambhav.tws.ui.home.exam.model

import android.widget.RadioButton

data class AttempQuestions(
    var test_id:Int=0,
    var question_id:Int=0,
    var user_input:String="",
    var questionType:String=""

    //UI



)
data class AtttempUI(
    var radioButtonId:String="",
    var natTypeValue:String="",
    var checkBox1:String="",
    var checkBox2:String="",
    var checkBox3:String="",
    var checkBox4:String="",
    var checkBox5:String=""
)



