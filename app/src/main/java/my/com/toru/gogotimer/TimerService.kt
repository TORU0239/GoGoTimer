package my.com.toru.gogotimer

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.CountDownTimer
import android.os.Handler
import android.os.IBinder
import android.util.Log

class TimerService : Service() {

    private val myBinder = TimerBinder()

    override fun onBind(intent: Intent): IBinder = myBinder

    inner class TimerBinder : Binder() {
        fun getTimerBinder():TimerService = this@TimerService
    }

    private lateinit var countDownTimer: CountDownTimer

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.w("TimerService", "onStartCommand")
        countDownTimer = MyCountDownTimer(10000, 1000)
        countDownTimer.start()
        return START_STICKY
    }

    inner class MyCountDownTimer(millisInFuture:Long, countdownInterval:Long) : CountDownTimer(millisInFuture, countdownInterval){
        override fun onFinish() {
            Log.w("TimerService", "finished!!")
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.w("TimerService", "tick::$millisUntilFinished")
            stopSelf()
        }
    }

    override fun onDestroy() {
        Log.w("TimerService", "onDestroy")
        super.onDestroy()
    }
}
