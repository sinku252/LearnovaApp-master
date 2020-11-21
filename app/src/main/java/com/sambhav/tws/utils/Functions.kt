import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.net.ConnectivityManager
import android.util.DisplayMetrics
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.sambhav.tws.R
import com.sambhav.tws.ui.home.notes.model.NotesSubListModel
import com.sambhav.tws.utils.*
import kotlinx.android.synthetic.main.dialog_session_end.*
import java.io.File
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


fun isDocDownload(notes: NotesSubListModel): Boolean {
    var exe = notes.id+notes.src_url
    val j = exe.lastIndexOf(".")
    if (j >= 0) {
        exe = notes.id+exe.substring(j)
    }
    return File(BASE_DOC_RECEIVE_PATH + "DOC_$exe").exists()
}


fun Context.getSessionDialog(success: () -> Unit): Dialog {
    return Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
        setContentView(R.layout.dialog_session_end)
        show()
        btn_submit.setOnClickListener(CustomClickListener {
            success()
            dismiss()
        })
    }
}

fun Context.getFullScreenDialog(layout: Int): Dialog {
    return Dialog(this, android.R.style.Theme_Light_NoTitleBar_Fullscreen).apply {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(layout)
    }
}

fun Context.getAlertDailog(
    title: String, msg: String,
    success: () -> Unit,
    failed: () -> Unit = {}
) {
    AlertDialog.Builder(this)
        .setTitle(title)
        .setMessage(msg)
        .setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0?.dismiss()
                success()
            }
        })
        .setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                p0?.dismiss()
                failed()
            }
        })
        .create().show()
}

fun isConnected(context: Context?): Boolean {
    val manager: ConnectivityManager? =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
    val info = manager?.activeNetworkInfo
    return !(info == null || !info.isConnected)
}

fun String?.getChatTime(): String {
    val newFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())

    if (this == "" || this == null || this == "null") {
        val cal = Calendar.getInstance()
        return newFormat.format(cal.timeInMillis)
    } else {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.getDefault())
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val date = dateFormat.parse(this) ?: Date()
        newFormat.timeZone = TimeZone.getDefault()
        return newFormat.format(date)
    }
}

fun String?.getDateAndTime(): String {
    val newFormat = SimpleDateFormat("dd-MM-yyyy hh:MM:ss", Locale.getDefault())

    if (this == "" || this == null || this == "null") {
        val cal = Calendar.getInstance()
        return newFormat.format(cal.timeInMillis)
    } else {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.getDefault())
        val date = dateFormat.parse(this) ?: Date()
        return newFormat.format(date)
    }
}

fun String?.getDate(): String {
    val newFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    if (this == "" || this == null || this == "null") {
        val cal = Calendar.getInstance()
        return newFormat.format(cal.timeInMillis)
    } else {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val date = dateFormat.parse(this) ?: Date()
        return newFormat.format(date)
    }
}

fun getDateWithMonthName(testDate: String): String {
   // finalDate=SimpleDateFormat("MMM dd, yyyy").format(secondSimpleDateFormat.parse(testDate))
    val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm",
        Locale.ENGLISH
    )
    val df: DateFormat = SimpleDateFormat("MMM dd, yyyy | hh:mm aaa" , Locale.ENGLISH)
    val date: Date = sdf.parse(testDate) // converting String to date
    return df.format(date)
    //System.out.println(df.format(date))
}

fun getTime(testDate: String): String {
    var finalDate:String=""
    val secondSimpleDateFormat =SimpleDateFormat("hh:mm aaa")
    try {
        finalDate=secondSimpleDateFormat.format(testDate)
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    return finalDate
}

fun String?.getScheduleTime(): String {
    val newFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
    if (this == "" || this == null || this == "null") {
        val cal = Calendar.getInstance()
        return newFormat.format(cal.timeInMillis)
    } else {
        val dateFormat = SimpleDateFormat("hh:mm:ss", Locale.getDefault())
        val date = dateFormat.parse(this) ?: Date()
        return newFormat.format(date)
    }
}

fun String?.compareDate(): String {
    if (this == "" || this == null || this == "null") {
        return "Today"
    }
    val cal = Calendar.getInstance()
    val today = cal.timeInMillis
    cal.add(Calendar.DAY_OF_MONTH, Calendar.DAY_OF_MONTH - 1)
    val yesterday = cal.timeInMillis

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    val newFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    val date = dateFormat.parse(this) ?: Date()
    val strDate = newFormat.format(date)

    try {
        Log.d("compareDate", "$strDate " + date.time * 1000)
    } catch (e: Exception) {
        Log.d("compareDate", "Exception  ")
    }
    return if (strDate == newFormat.format(today))
        "Today" else if (strDate == newFormat.format(yesterday)) "Yesterday" else strDate
}

fun String?.compareEvent(): String {
    if (this == "" || this == null || this == "null") {
        return TODAY_EVENT
    }
    val cal = Calendar.getInstance()
    cal.set(Calendar.MINUTE,2)
    cal.set(Calendar.HOUR_OF_DAY,0)
    val today = cal.timeInMillis

    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    val date = dateFormat.parse(this) ?: Date()
    val strDate = dateFormat.format(date)

    return if (strDate == dateFormat.format(today))
        TODAY_EVENT
    else if (date.time < cal.timeInMillis)
        PAST_EVENT else
        UPCOMIG_EVENT
}

fun String?.getEvent(): String {
    if (this == "" || this == null || this == "null") {
        return TODAY_EVENT
    }

    val cal = Calendar.getInstance()
    cal.set(Calendar.MINUTE,2)
    cal.set(Calendar.HOUR_OF_DAY,0)
    cal.add(Calendar.DAY_OF_MONTH,1)
    val today = System.currentTimeMillis()
    val nextDay = cal.timeInMillis

    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = dateFormat.parse(this) ?: Date()
    val strDate = dateFormat.format(date)

    Log.d("canStartClass","${date.time} current $today date ${nextDay}")

    return if (date.time in today..nextDay)
        TODAY_EVENT
    else if (date.time < cal.timeInMillis)
        PAST_EVENT else
        UPCOMIG_EVENT
}

fun getClassTime(classDate: String?,classTime: String?): Long {
    val eDate = "$classDate $classTime"
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = dateFormat.parse(eDate) ?: Date()
    return date.time
}

fun getDeviceRatio(context: Context): Pair<Int, Int> {
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    val displayMetrics = DisplayMetrics()
    windowManager.defaultDisplay?.getMetrics(displayMetrics)

    return Pair(displayMetrics.widthPixels, displayMetrics.heightPixels)
}

fun canStartClass(classDate: String?,classTime: String?): Boolean {
    val eDate = "$classDate $classTime"
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = dateFormat.parse(eDate) ?: Date()
    val cal = Calendar.getInstance()
    cal.set(Calendar.SECOND ,0)
    cal.set(Calendar.MILLISECOND ,0)
    val current = cal.timeInMillis
    cal.add(Calendar.HOUR_OF_DAY, 1)
    val nextHour = cal.timeInMillis
    Log.d("canStartClass","$nextHour current $current date ${date.time}")
    Log.d("canStartClass","$classDate  $classTime ${(date.time <nextHour)} ${(date.time >= current)}")
    return (date.time in current..nextHour)
}

fun getBackColorForClass(classDate: String?, classTime: String?): String {
    val eDate = "$classDate $classTime"
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = dateFormat.parse(eDate) ?: Date()
    val cal = Calendar.getInstance()
    cal.set(Calendar.SECOND ,0)
    cal.set(Calendar.MILLISECOND ,0)
    val current = cal.timeInMillis
    cal.add(Calendar.HOUR_OF_DAY, 1)
    val nextHour = cal.timeInMillis
    Log.d("BackColorForClass","$classTime current $current date ${date.time}")
    val eventHeader = classDate.compareEvent()
    return when {
        (date.time in current..nextHour) -> "#4CAF50" //green
        (date.time < current) -> "#DBE9FF" //light
        (eventHeader == TODAY_EVENT) -> "#3F51B5" //blue
        (eventHeader == PAST_EVENT) -> "#DBE9FF" //light
        (eventHeader == UPCOMIG_EVENT) -> "#FFC107" //yellow
        else -> "#DBE9FF" //light
    }
}


fun getTextColorForClass(classDate: String?, classTime: String?): String {
    val eDate = "$classDate $classTime"
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = dateFormat.parse(eDate) ?: Date()
    val cal = Calendar.getInstance()
    cal.set(Calendar.SECOND ,0)
    cal.set(Calendar.MILLISECOND ,0)
    val current = cal.timeInMillis
    cal.add(Calendar.HOUR_OF_DAY, 1)
    val nextHour = cal.timeInMillis
    Log.d("BackColorForClass","$classTime current $current date ${date.time}")
    val eventHeader = classDate.compareEvent()
    return when {
        (date.time in current..nextHour) -> "#FFFFFF" //green
        (date.time < current) -> "#000000" //light
        (eventHeader == TODAY_EVENT) -> "#FFFFFF" //blue
        (eventHeader == PAST_EVENT) -> "#000000" //light
        (eventHeader == UPCOMIG_EVENT) -> "#000000" //yellow
        else -> "#000000" //light
    }
}


fun getTextColorForClass2(classDate: String?, classTime: String?): String {
    val eDate = "$classDate $classTime"
    val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault())
    val date = dateFormat.parse(eDate) ?: Date()
    val cal = Calendar.getInstance()
    cal.set(Calendar.SECOND ,0)
    cal.set(Calendar.MILLISECOND ,0)
    val current = cal.timeInMillis
    cal.add(Calendar.HOUR_OF_DAY, 1)
    val nextHour = cal.timeInMillis
    Log.d("BackColorForClass","$classTime current $current date ${date.time}")
    val eventHeader = classDate.compareEvent()
    return when {
        (date.time in current..nextHour) -> "#FFFFFF" //green
        (date.time < current) -> "#DB626262" //light
        (eventHeader == TODAY_EVENT) -> "#FFFFFF" //blue
        (eventHeader == PAST_EVENT) -> "#DB626262" //light
        (eventHeader == UPCOMIG_EVENT) -> "#DB626262" //yellow
        else -> "#DB626262" //light
    }


    fun getOptionFromIndex(index: Int?):String {
        var temp:String=""
        if(index==0)
            temp="option_1_text"
        else if(index==1)
            temp="option_2_text"
        else if(index==2)
            temp="option_3_text"
        else if(index==3)
            temp="option_4_text"
        else if(index==4)
            temp="option_5_text"
        return temp
    }
    /*fun getOptionFromIndex(index: Int) {
        var temp:String="";
        if(index==0)
            temp="option_1_text"
        else if(index==1)
            temp="option_2_text"
        else if(index==2)
            temp="option_3_text"
        else if(index==3)
            temp="option_4_text"
        else if(index==4)
            temp="option_5_text"
        //return temp
    }*/


}