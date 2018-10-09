package my.com.toru.gogotimer.service

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.support.v4.app.NotificationCompat
import android.util.Log
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.model.TimerHistoryData
import my.com.toru.gogotimer.ui.main.MainActivity
import my.com.toru.gogotimer.ui.main.generate
import my.com.toru.gogotimer.util.CONST_CHANNEL_NAME
import my.com.toru.gogotimer.util.CONST_FINISHED
import my.com.toru.gogotimer.util.SEND_DATA_TASK_NAME

class AlarmReceiver:BroadcastReceiver() {
    companion object {
        private val TAG = AlarmReceiver::class.java.simpleName
    }
    override fun onReceive(context: Context?, intent: Intent?) {
        when(intent?.action){
            CONST_FINISHED->{
                context?.let{ ctx->
                    with(receiver = intent.getStringExtra(SEND_DATA_TASK_NAME)){
                        sendNotification(ctx, this)
                        saveData(this)
                    }
                }
            }
            else->{
                Log.w(TAG, "Do Nothing!!")
            }
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

    private fun sendNotification(context:Context, taskName:String){
        val intent = Intent(context, MainActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
        val pendingIntent = PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val notification = NotificationCompat.Builder(context, CONST_CHANNEL_NAME)
        val noti = notification.generate("$taskName LAUNCHED!!!",
                "GOGOALARM",
                NotificationCompat.PRIORITY_MAX,
                pendingIntent)

        val notiManager = context.applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notiManager.notify(1024, noti)
    }
}