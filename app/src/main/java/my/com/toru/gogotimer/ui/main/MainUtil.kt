package my.com.toru.gogotimer.ui.main

import android.app.Notification
import android.app.PendingIntent
import android.databinding.BindingAdapter
import android.support.v4.app.NotificationCompat
import android.widget.ImageView
import android.widget.TextView
import my.com.toru.gogotimer.R


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
                                        priority:Int,
                                        pendingIntent:PendingIntent): Notification
        = this.setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(contentTxt)
        .setContentTitle(contentTitle)
        .setAutoCancel(true)
        .setPriority(priority)
        .setFullScreenIntent(pendingIntent, true)
        .build()