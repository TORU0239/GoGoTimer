package my.com.toru.gogotimer

import android.annotation.SuppressLint
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Message
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
        val second = intent?.getIntExtra("SECOND", 10 * 1000)!!
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val noti = NotificationCompat.Builder(this, "GOGOTIMER_CHANNEL")
            noti.setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("GoGoTimer")
                .setContentTitle("My notification")
                .setContentText("Hello World!")
                .setAutoCancel(true)
                .priority = NotificationCompat.PRIORITY_LOW
            startForeground(1004, noti.build())
        }

        val msg = Message()
        msg.apply{
            what = 1024
            arg1 = second
        }
        handler.sendMessage(msg)
        return START_STICKY
    }

    @SuppressLint("HandlerLeak")
    inner class MyHandler: Handler(){
        override fun handleMessage(msg: Message?) {
            when(msg?.what){
                1024->{
                    if(msg.arg1 == 0){
                        Log.w("TimerService", "handler finished!!")
                        val intent = Intent(this@TimerService, MainActivity::class.java)
                                                .addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        val pendingIntent = PendingIntent.getActivity(this@TimerService, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        val noti = NotificationCompat.Builder(this@TimerService, "GOGOTIMER_CHANNEL")
                        noti.setSmallIcon(R.mipmap.ic_launcher)
                                .setContentText("GoGoTimer")
                                .setContentTitle("My notification")
                                .setContentText("Alarm Finished!!!")
                                .setVibrate(longArrayOf(0))
                                .setAutoCancel(true)
                                .setTicker("My Notification")
                                .setFullScreenIntent(pendingIntent, true)
                                .priority = NotificationCompat.PRIORITY_MAX

                        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notiManager.notify(1024, noti.build())

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