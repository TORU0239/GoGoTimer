package my.com.toru.gogotimer.service

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
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.ui.main.MainActivity
import my.com.toru.gogotimer.util.*


class TimerService : Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    private lateinit var handler: MyHandler

    override fun onCreate() {
        super.onCreate()
        handler = MyHandler()
    }

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
                        val intent = Intent(this@TimerService, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                        val pendingIntent = PendingIntent.getActivity(this@TimerService, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
                        val noti = NotificationCompat.Builder(this@TimerService, "GOGOTIMER_CHANNEL")
                        noti.setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle("GoGoTimer")
                                .setContentText("Alarm Finished!!!")
                                .setVibrate(longArrayOf(0))
                                .setAutoCancel(true)
                                .setTicker("Alarm Finished!!!")
                                .setFullScreenIntent(pendingIntent, true)
                                .priority = NotificationCompat.PRIORITY_MAX

                        val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                        notiManager.notify(1024, noti.build())
                        sendBroadcast(Intent(CONST_FINISHED))
                        stopSelf()
                    }
                    else{
                        val newMsg = Message()
                        newMsg.apply {
                            what = 1024
                            arg1 = (msg.arg1 - 1000)
                            val intent = Intent(CONST_UPDATE)
                            intent.putExtra(CONST_HOURS, ((msg.arg1 - 1000) / 1000) / 3600)
                            intent.putExtra(CONST_MINUTES, ((msg.arg1 - 1000) / 1000) / 60)
                            intent.putExtra(CONST_SECONDS, ((msg.arg1 - 1000) / 1000) % 60)
                            sendBroadcast(intent)
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

    private fun NotificationCompat.Builder.generate(contentTxt:String,
                                                    contentTitle:String,
                                                    priority:Int):NotificationCompat.Builder
            = this.setSmallIcon(R.mipmap.ic_launcher)
            .setContentText(contentTxt)
            .setContentTitle(contentTitle)
            .setAutoCancel(true)
            .setPriority(priority)
}