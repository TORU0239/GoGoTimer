package my.com.toru.gogotimer

import android.app.Service
import android.content.Intent
import android.os.*
import android.support.v4.app.NotificationCompat
import android.util.Log

class TimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    private lateinit var handler:MyHandler

    override fun onCreate() {
        super.onCreate()
        handler = MyHandler()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w("TimerService", "onStartCommand")
        var second = intent?.getIntExtra("SECOND", 0)

        val noti = NotificationCompat.Builder(this, "GOGOTIMER_CHANNEL")
        noti.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("GoGoTimer")
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setAutoCancel(true)
                .priority = NotificationCompat.PRIORITY_DEFAULT
        startForeground(1004, noti.build())

        val msg = Message()
        msg.apply{
            what = 1024
            arg1 = 10000
        }
        handler.sendMessage(msg)
        return START_STICKY
    }

    inner class MyHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                1024->{
                    if(msg.arg1 == 0){
                        Log.w("TimerService", "handler finished!!")
                        stopSelf()
                    }
                    else{
                        Log.w("TimerService", "handler!!")
                        val newMsg = Message()
                        newMsg.apply {
                            what = 1024
                            arg1 = (msg.arg1 - 1000)
                        }
                        sendMessageDelayed(newMsg, 1000)
                    }
                }
                else->super.handleMessage(msg)
            }

        }
    }

    override fun onDestroy() {
        Log.w("TimerService", "onDestroy")
        super.onDestroy()
        handler.removeMessages(1024)
    }
}