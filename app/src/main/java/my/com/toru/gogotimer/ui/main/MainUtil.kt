package my.com.toru.gogotimer.ui.main

import android.databinding.BindingAdapter
import android.support.v4.app.NotificationCompat
import android.widget.ImageView
import android.widget.TextView
import my.com.toru.gogotimer.R

fun TextView.getInteger():Int = Integer.parseInt(this.text as String)

fun TextView.setFormattedDigit(digit:Int){
    this.text = String.format("%1$02d", digit)
}

@BindingAdapter("digit")
fun TextView.setDigit(digit:Int){
    this.text = String.format("%1$02d", digit)
}

@BindingAdapter("triggerIcon")
fun ImageView.setTriggerIcon(isTimerTriggered:Boolean){
    if(isTimerTriggered){
        this.setImageResource(R.drawable.ic_outline_pause_24px)
    }
    else{
        this.setImageResource(R.drawable.ic_outline_arrow_forward_ios_24px)
    }
}

fun NotificationCompat.Builder.generate(contentTxt:String,
                                        contentTitle:String,
                                        priority:Int): NotificationCompat.Builder
        = this.setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(contentTxt)
        .setContentTitle(contentTitle)
        .setAutoCancel(true)
        .setPriority(priority)