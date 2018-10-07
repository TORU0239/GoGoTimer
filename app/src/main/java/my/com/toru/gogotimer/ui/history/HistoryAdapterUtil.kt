package my.com.toru.gogotimer.ui.history

import android.databinding.BindingAdapter
import android.widget.ImageView
import android.widget.TextView
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.model.TimerHistoryData

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
    if(model.taskStartTimeStamp == 0L){
        this.text = model.taskEndTimeStamp.toString()
    }
    else{
        this.text = model.taskStartTimeStamp.toString()
    }

}