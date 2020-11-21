package com.sambhav.tws.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Patterns
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.gson.Gson
import com.sambhav.tws.R
import com.sambhav.tws.ui.SplashActivity
import com.sambhav.tws.ui.home.doubt.AllDoubtActivity
import com.sambhav.tws.ui.home.doubt.model.NotificationModel
import com.sambhav.tws.ui.home.videos.activities.LiveVideoActivity
import com.sambhav.tws.ui.login.model.StudentModel
import com.sambhav.tws.ui.login.model.TeacherModel
import com.sambhav.tws.ui.schedule.activities.ViewScheduleActivity
import java.text.SimpleDateFormat
import java.util.*


fun isValueNotEmpty(value: String): Boolean {
    return value.isNullOrBlank()
}

fun getStudentData(mPreference: SharedPreferencesHelper): StudentModel {
    return Gson().fromJson(mPreference.studentData, StudentModel::class.java)
}

fun getTeacherData(mPreference: SharedPreferencesHelper): TeacherModel {
    return Gson().fromJson(mPreference.teacherData, TeacherModel::class.java)
}

fun CharSequence?.isValidEmail() =
    !isNullOrEmpty() && Patterns.EMAIL_ADDRESS.matcher(this).matches()

fun String?.changeDate(): String {
    val newFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    if (this == "" || this == null || this == "null") {
        val cal = Calendar.getInstance()
        return newFormat.format(cal.timeInMillis)
    } else {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(this) ?: Date()
        return newFormat.format(date)
    }
}

fun removeExtraCommas(entry: String?): String {
    var entry = entry
    var ret = ""
    entry = entry?.replace("\\s".toRegex(), "")
    val arr = entry?.split(",".toRegex())?.toTypedArray()
    var start = true
    if (arr != null) {
        for (str in arr) {
            if (!"".equals(str, ignoreCase = true)) {
                if (start) {
                    ret = str
                    start = false
                } else {
                    ret = "$ret,$str"
                }
            }
        }
    }
    return ret
}


fun String?.changeTime(): String {
    val newFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    //newFormat.timeZone = TimeZone.getDefault();
    if (this == "" || this == null || this == "null") {
        val cal = Calendar.getInstance()
        return newFormat.format(cal.timeInMillis)
    } else {
        val dateFormat = SimpleDateFormat("yyyy-mm-dd hh:mm:ss",Locale.getDefault())
       // newFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(this) ?: Date()
        return newFormat.format(date)
    }
}


const val CHANNEL_ID = "com.kotafactory.app"
const val CHANNEL = "Doubt"
fun Context.showNotification(mNotificationModel: NotificationModel) {
    var mNotificationManager: NotificationManager? = null
    var builder: NotificationCompat.Builder? = null

    val id = generateRandom()
    mNotificationManager = this
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    val intent: Intent = when {
        mNotificationModel.type == NOTIFICATION_TYPE_KEY_DOUBT -> Intent(
            this,
            AllDoubtActivity::class.java
        )
        mNotificationModel.type == NOTIFICATION_TYPE_KEY_VIDEO -> Intent(
            this,
            LiveVideoActivity::class.java
        )
        mNotificationModel.type == NOTIFICATION_TYPE_KEY_NOTE -> Intent(
            this,
            SplashActivity::class.java
        )
        mNotificationModel.type == NOTIFICATION_TYPE_KEY_LIVE_CLASS -> Intent(
            this,
            ViewScheduleActivity::class.java
        )
        else -> Intent(this, SplashActivity::class.java)
    }
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
    intent.putExtra("icon", mNotificationModel.subject_icon)
    intent.putExtra("docId", mNotificationModel.document_library_id)
    intent.putExtra("roomId", mNotificationModel.student_id)
    intent.putExtra(EXTRA_KEY_SUBJECT_ID, mNotificationModel.subject_id)
    intent.putExtra(EXTRA_KEY_SUBJECT_NAME, mNotificationModel.subject_name)
    val contentIntent = PendingIntent.getActivity(
        this, generateRandom(),
        intent, PendingIntent.FLAG_UPDATE_CURRENT
    )
    builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        setNotificationCompatChanel()
        NotificationCompat.Builder(this, CHANNEL_ID)
    } else {
        NotificationCompat.Builder(this)
    }
    val bit = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
    builder.setSmallIcon(R.mipmap.ic_launcher)
    builder.setLargeIcon(bit)
    builder.color = ContextCompat.getColor(this, R.color.colorPrimary)
    builder.setContentTitle(getString(R.string.app_name))
    builder.setContentText(mNotificationModel.body)
    builder.setStyle(NotificationCompat.BigTextStyle().bigText(mNotificationModel.body))
    builder.setContentIntent(contentIntent)
    builder.setAutoCancel(true)
    mNotificationManager.notify(id, builder.build())
}

fun generateRandom(): Int {
    return ((Math.random() * 100).toInt())
}

@RequiresApi(api = Build.VERSION_CODES.O)
fun Context.setNotificationCompatChanel() {
    val importance = NotificationManager.IMPORTANCE_DEFAULT
    val notificationChannel = NotificationChannel(
        CHANNEL_ID, CHANNEL, importance
    )
    notificationChannel.enableLights(true)
    notificationChannel.lightColor = Color.RED
    notificationChannel.enableVibration(true)
    notificationChannel.vibrationPattern =
        longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(notificationChannel)
}

fun fromHtml(source: String?): Spanned? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(source)
    }
}
