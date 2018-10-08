package my.com.toru.gogotimer.service

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.Message
import android.support.v4.app.NotificationCompat
import android.util.Log
import my.com.toru.gogotimer.ui.main.generate


class TimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w("TimerService", "onStartCommand")

        val second = intent?.getIntExtra("SECOND", 0)!!
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val noti = NotificationCompat.Builder(this, "GOGOTIMER_CHANNEL")
            val notification = noti.generate("GoGoTimer", "AlarmStarted", NotificationCompat.PRIORITY_LOW)
                                            .build()
            startForeground(1004, notification)
        }

        val msg = Message()
        msg.apply{
            what = 1024
            arg1 = second
        }
        return START_STICKY
    }

    override fun onDestroy() {
        Log.w("TimerService", "onDestroy")
        super.onDestroy()
    }
}