package my.com.toru.gogotimer.ui.history

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.model.TimerHistoryData
import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

@BindingAdapter("icon")
fun ImageView.setIcon(startTime: Long) {
    if(startTime == 0L){
        this.setImageResource(R.drawable.ic_baseline_alarm_off_24px)
    }
    else{
        this.setImageResource(R.drawable.ic_baseline_alarm_on_24px)
    }
}

@BindingAdapter("timeStamp")
fun TextView.setTimeStamp(model:TimerHistoryData){
    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val stamp:Timestamp = if(model.taskStartTimeStamp == 0L){
        Timestamp(model.taskEndTimeStamp)
    } else{
        Timestamp(model.taskStartTimeStamp)
    }
    val date = Date(stamp.time)
    this.text = sdf.format(date)
}