package my.com.toru.gogotimer.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.model.TimerHistoryData
import my.com.toru.gogotimer.ui.main.MainActivity
import my.com.toru.gogotimer.util.CONST_CHANNEL_NAME

class AlarmReceiver:BroadcastReceiver() {
    companion object {
        private val TAG = AlarmReceiver::class.java.simpleName
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let{
            sendNotification(it)
            saveData(intent?.getStringExtra("SEND_DATA_TASKNAME"))
        }
    }

    private fun saveData(currentTaskName:String?){
        val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
        val dao = db?.timerHistoryDao()
        val historyData = TimerHistoryData()
        historyData.apply {
            currentTaskName?.let {
                taskName = it
                taskEndTimeStamp = System.currentTimeMillis()
            }
        }
        dao?.insertData(historyData)
    }

    private fun sendNotification(context:Context){
        val notification = NotificationCompat.Builder(context, CONST_CHANNEL_NAME)
        val intent = Intent(context , MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        val pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val noti = notification.setContentTitle("TEST")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("TRIGGERED!!!")
                .setAutoCancel(true)
                .setFullScreenIntent(pendingIntent, true)
                .build()
        val notiManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notiManager.notify(1024, noti)
    }
}