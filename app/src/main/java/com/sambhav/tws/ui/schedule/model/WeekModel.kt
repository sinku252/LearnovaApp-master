package com.sambhav.tws.ui.schedule.model

data class WeekModel(
    var name:String,
    var value:String,
    var select:Boolean =false
){
    fun selectWeek(check:Boolean){
        select = check
    }
}