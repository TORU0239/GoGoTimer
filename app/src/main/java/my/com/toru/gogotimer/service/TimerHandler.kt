package my.com.toru.gogotimer.service

import android.content.Intent
import android.os.Handler
import android.os.Message
import android.util.Log
import my.com.toru.gogotimer.util.CONST_HOURS
import my.com.toru.gogotimer.util.CONST_MINUTES
import my.com.toru.gogotimer.util.CONST_SECONDS
import my.com.toru.gogotimer.util.CONST_UPDATE

class TimerHandler:Handler() {
    companion object {
        private val TAG = TimerHandler::class.java.simpleName
    }

    override fun handleMessage(msg: Message?) {
        when (msg?.what) {
            1024 -> {
                if (msg.arg1 == 0) {
                    Log.w(TAG, "handler finished!!")
//                    val intent = Intent(this@TimerService , MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
//                    val pendingIntent = PendingIntent.getActivity(this@TimerService, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//                    val noti = NotificationCompat.Builder(this@TimerService, "GOGOTIMER_CHANNEL")
//                    noti.setSmallIcon(R.mipmap.ic_launcher)
//                            .setContentTitle("GoGoTimer")
//                            .setContentText("Alarm Finished!!!")
//                            .setVibrate(longArrayOf(0))
//                            .setAutoCancel(true)
//                            .setTicker("Alarm Finished!!!")
//                            .setFullScreenIntent(pendingIntent, true)
//                            .priority = NotificationCompat.PRIORITY_MAX
//
//                    val notiManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                    notiManager.notify(1024, noti.build())
//                    sendBroadcast(Intent(CONST_FINISHED))
//                    stopSelf()
                }
                else {
                    val newMsg = Message()
                    newMsg.apply {
                        what = 1024
                        arg1 = (msg.arg1 - 1000)
                        val intent = Intent(CONST_UPDATE)
                        intent.putExtra(CONST_HOURS, ((msg.arg1 - 1000) / 1000) / 3600)
                        intent.putExtra(CONST_MINUTES, ((msg.arg1 - 1000) / 1000) / 60)
                        intent.putExtra(CONST_SECONDS, ((msg.arg1 - 1000) / 1000) % 60)
//                        sendBroadcast(intent)
                    }
                    sendMessageDelayed(newMsg, 1000)
                }
            }
            else -> super.handleMessage(msg)
        }
    }
}