package com.sambhav.tws.utils

import com.sambhav.tws.R
import com.sambhav.tws.apiModel.LiveClassData
import com.sambhav.tws.ui.home.home.model.MenuModel
import com.sambhav.tws.ui.home.classes.model.TeacherMenuModel
import com.sambhav.tws.ui.home.doubt.model.DoubtRoomModel
import com.sambhav.tws.ui.home.notes.model.NotesSubListModel
import com.sambhav.tws.ui.home.notes.model.NotesSubModel
import com.sambhav.tws.ui.home.videos.model.VideosListModel
import com.sambhav.tws.ui.home.videos.model.VideosSubModel
import com.sambhav.tws.ui.schedule.model.WeekModel
import com.sambhav.tws.ui.subject.model.SubjectData


fun getEvents(): ArrayList<LiveClassData> {
    return ArrayList<LiveClassData>().apply {
        add(
            LiveClassData(
                subject_title = "Today's Event"
            )
        )

        add(
            LiveClassData(
                subject_title = "SCIENCE",
                teacherName = "Tapsi",
                teacher_image= DUMMY_URL
            )
        )

        add(
            LiveClassData(
                subject_title = "MATH",
                teacherName = "Tapsi",
                teacher_image = DUMMY_URL
            )
        )

        add(
            LiveClassData(
                subject_title = "Upcomig Events"
            )
        )

        add(
            LiveClassData(
                subject_title = "ARTS",
                teacherName = "Tapsi",
                teacher_image = DUMMY_URL
            )
        )
        add(
            LiveClassData(
                subject_title = "SST",
                teacherName = "Tapsi",
                teacher_image = DUMMY_URL
            )
        )
    }

}



fun getNotesSub(): ArrayList<NotesSubModel> {
    return ArrayList<NotesSubModel>().apply {
        add(
            NotesSubModel(
                subject = "SCIENCE",
                subIcon = R.drawable.ic_math,
                primaryColor = "#000000",
                backColor = "#DBE9FF"
            )
        )

        add(
            NotesSubModel(
                subject = "MATH",
                subIcon = R.drawable.ic_math,
                primaryColor = "#000000",
                backColor = "#4CAF50"
            )
        )
        add(
            NotesSubModel(
                subject = "ARTS",
                subIcon = R.drawable.ic_math,
                primaryColor = "#000000",
                backColor = "#207DFF"
            )
        )
        add(
            NotesSubModel(
                subject = "SST",
                subIcon = R.drawable.ic_math,
                primaryColor = "#000000",
                backColor = "#FFC107"
            )
        )

        add(
            NotesSubModel(
                subject = "SCIENCE",
                subIcon = R.drawable.ic_math,
                primaryColor = "#000000",
                backColor = "#DBE9FF"
            )
        )
        add(
            NotesSubModel(
                subject = "ARTS",
                subIcon = R.drawable.ic_math,
                primaryColor = "#000000",
                backColor = "#207DFF"
            )
        )

    }

}

fun getNotesList(): ArrayList<NotesSubListModel> {
    return ArrayList<NotesSubListModel>().apply {
        add(
            NotesSubListModel(
                title = "Today"
            )
        )

        add(
            NotesSubListModel(
                title = "PDF - IMP Questions",
                primaryColor = "#000000",
                backColor = "#DBE9FF"
            )
        )

        add(
            NotesSubListModel(
                title = "Previous"
            )
        )

        add(
            NotesSubListModel(
                title = "MATH",
                primaryColor = "#000000",
                backColor = "#4CAF50",
                isDownload = true
            )
        )
        add(
            NotesSubListModel(
                title = "ARTS",
                primaryColor = "#000000",
                backColor = "#207DFF",
                isDownload = true
            )
        )
        add(
            NotesSubListModel(
                title = "SST",
                primaryColor = "#000000",
                backColor = "#FFC107"
            )
        )
    }

}

fun getVideosSub(): ArrayList<VideosSubModel> {
    return ArrayList<VideosSubModel>().apply {
        add(
            VideosSubModel(
                subject = "SCIENCE",
                subIcon = R.drawable.ic_math,
                backColor = "#DBE9FF"
            )
        )

        add(
            VideosSubModel(
                subject = "MATH",
                subIcon = R.drawable.ic_math,
                backColor = "#4CAF50"
            )
        )
        add(
            VideosSubModel(
                subject = "ARTS",
                subIcon = R.drawable.ic_math,
                backColor = "#207DFF"
            )
        )
        add(
            VideosSubModel(
                subject = "SST",
                subIcon = R.drawable.ic_math,
                backColor = "#FFC107"
            )
        )

        add(
            VideosSubModel(
                subject = "SCIENCE",
                subIcon = R.drawable.ic_math,
                backColor = "#DBE9FF"
            )
        )

        add(
            VideosSubModel(
                subject = "MATH",
                subIcon = R.drawable.ic_math,
                backColor = "#4CAF50"
            )
        )
    }
}

fun getTeacherMenuList(): ArrayList<TeacherMenuModel> {
    return ArrayList<TeacherMenuModel>().apply {
        add(
            TeacherMenuModel(
                subject = "SCHEDULE A CLASS",
                subIcon = R.drawable.ic_schedule_class,
                backColor = R.drawable.bg_schedule_class_gradient
            )
        )

        add(
            TeacherMenuModel(
                subject = "START A CLASS",
                subIcon = R.drawable.ic_start_class,
                backColor = R.drawable.bg_start_class_gradient
            )
        )
        add(
            TeacherMenuModel(
                subject = "VIEW SCHEDULE",
                subIcon = R.drawable.ic_view_schedule,
                backColor = R.drawable.bg_view_schedule_gradient
            )
        )
    }
}

fun getVideosList(): ArrayList<VideosListModel> {
    val list =  ArrayList<VideosListModel>()
    for (i in 0 until 10){
        if(i==0 || i == 3){
            if(i==0){
                list.add(
                    VideosListModel(
                        subject = "Today",
                        fileName = ""
                    )
                )
            }else{
                list.add(
                    VideosListModel(
                        subject = "Previous",
                        fileName = ""
                    )
                )
            }
        }else{
            list.add(
                VideosListModel(
                    subject = "Previous",
                    fileName = "Previous",
                    teacherImage = DUMMY_URL
                )
            )
        }
    }

    return list

}
fun getMenuList(type:Boolean) : ArrayList<MenuModel>{
    return ArrayList<MenuModel>().apply {
        add(MenuModel(name = Live,
            icon = R.drawable.ic_live_class,
            textColor = "#000000",
            bgColor = "#DBE9FF"))
        add(MenuModel(name = Video,
            icon = R.drawable.ic_video_library,
            textColor = "#000000",
            bgColor = "#DBE9FF"))
        add(MenuModel(name = Notes,
            icon = R.drawable.ic_notes_and_resources,
            textColor = "#000000",
            bgColor = "#DBE9FF"))
        add(MenuModel(name = Doubt_Room,
            icon = R.drawable.ic_doubt_room,
            textColor = "#000000",
            bgColor = "#DBE9FF"))
        if(type)
        {
            add(MenuModel(name = Test_Series,
                icon = R.drawable.ic_test,
                textColor = "#000000",
                bgColor = "#DBE9FF"))
        }
    }
}
/*fun getNotificationList() : ArrayList<NotificationModel>{
    return ArrayList<NotificationModel>().apply {
        add(NotificationModel("Class is going to start in 30 min",
            "12:05 AM"))
        add(NotificationModel("Class is going to start in 30 min",
            "12:05 AM"))
        add(NotificationModel("Class is going to start in 30 min",
            "12:05 AM"))
        add(NotificationModel("Class is going to start in 30 min",
            "12:05 AM"))
    }
}*/


fun getSubjects(): ArrayList<SubjectData> {
    return ArrayList<SubjectData>().apply {
        add(
            SubjectData(
                subject_title = "Science"
            )
        )
        add(
            SubjectData(
                subject_title = "Math"
            )
        )

        add(
            SubjectData(
                subject_title = "Science"
            )
        )

        add(
            SubjectData(
                subName = "Math"
            )
        )
    }
}

fun getOccurance(): ArrayList<SubjectData> {
    return ArrayList<SubjectData>().apply {
        add(
            SubjectData(
                subject_title = "Single" ,
                subject_image= "no"
            )
        )
        add(
            SubjectData(
                subject_title = "Custom",
                subject_image= "no"
            )
        )
    }
}

fun getWeekDays():ArrayList<WeekModel>{
    return ArrayList<WeekModel>().apply {
        add(
            WeekModel(
                name = "S" ,
                value = "SUN"
            )
        )
        add(
            WeekModel(
                name = "M" ,
                value = "MON"
            )
        )
        add(
            WeekModel(
                name = "T" ,
                value = "TUE"
            )
        )
        add(
            WeekModel(
                name = "W" ,
                value = "WED"
            )
        )
        add(
            WeekModel(
                name = "T" ,
                value = "THU"
            )
        )
        add(
            WeekModel(
                name = "F" ,
                value = "FRI"
            )
        )
        add(
            WeekModel(
                name = "S" ,
                value = "SAT"
            )
        )
    }
}

fun getStudents(): ArrayList<DoubtRoomModel> {
    return ArrayList<DoubtRoomModel>().apply {
        add(DoubtRoomModel())
        add(DoubtRoomModel())
        add(DoubtRoomModel())
        add(DoubtRoomModel())
        add(DoubtRoomModel())
        add(DoubtRoomModel())
        add(DoubtRoomModel())
    }




}