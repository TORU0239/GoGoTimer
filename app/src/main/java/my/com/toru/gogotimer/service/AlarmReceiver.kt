package my.com.toru.gogotimer.service

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import android.widget.Toast
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.util.CONST_CHANNEL_NAME

class AlarmReceiver:BroadcastReceiver() {
    companion object {
        private val TAG = AlarmReceiver::class.java.simpleName
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let{
            Log.w(TAG, "onReceive End!!!!!!!!")
            Toast.makeText(it, "Alarm Triggered!", Toast.LENGTH_SHORT).show()
            sendNotification(it)
        }
    }

    fun sendNotification(context:Context){
        val notification = NotificationCompat.Builder(context, CONST_CHANNEL_NAME)
        val noti = notification.setContentTitle("TEST")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("TRIGGERED!!!")
                .setAutoCancel(true)
                .build()
        val notiManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notiManager.notify(1024, noti)
    }
}