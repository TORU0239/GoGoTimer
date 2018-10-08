package my.com.toru.gogotimer.ui.main

import android.databinding.BindingAdapter
import android.support.v4.app.NotificationCompat
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

fun NotificationCompat.Builder.generate(contentTxt:String,
                                        contentTitle:String,
                                        priority:Int): NotificationCompat.Builder
        = this.setSmallIcon(R.mipmap.ic_launcher)
        .setContentText(contentTxt)
        .setContentTitle(contentTitle)
        .setAutoCancel(true)
        .setPriority(priority)