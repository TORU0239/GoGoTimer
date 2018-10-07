package my.com.toru.gogotimer.ui.main

import android.widget.TextView

fun TextView.getInteger():Int = Integer.parseInt(this.text as String)

fun TextView.setFormattedDigit(digit:Int){
    this.text = String.format("%1$02d", digit)
}