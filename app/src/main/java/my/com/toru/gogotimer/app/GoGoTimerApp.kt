package my.com.toru.gogotimer.app

import android.annotation.SuppressLint
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class GoGoTimerApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: GoGoTimerApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
    override fun onCreate() {
        super.onCreate()
        configureNotificationChannel()
    }

    private fun configureNotificationChannel(){
       if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           val name = "GOGOTIMER"
           val descriptionText = "TIMER BY TORU"
           val importance = NotificationManager.IMPORTANCE_HIGH
           val channel = NotificationChannel("GOGOTIMER_CHANNEL", name, importance)
                   .apply {
                            description = descriptionText
                            }
           // Register the channel with the system
           val notificationManager: NotificationManager =
                   getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
           notificationManager.createNotificationChannel(channel)
       }
    }
}