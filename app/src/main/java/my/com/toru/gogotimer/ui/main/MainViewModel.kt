package my.com.toru.gogotimer.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Build
import android.os.SystemClock
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.service.AlarmReceiver
import my.com.toru.gogotimer.ui.history.HistoryFragment
import my.com.toru.gogotimer.ui.info.MyInfoFragment
import my.com.toru.gogotimer.util.CurrentStatus
import my.com.toru.gogotimer.util.Util

class MainViewModel{
    val hours = ObservableField<Int>()
    val minutes = ObservableField<Int>()
    val seconds = ObservableField<Int>()

    val isHoursChecked = ObservableBoolean(false)
    val isMinutesChecked = ObservableBoolean(false)
    val isSecondsChecked = ObservableBoolean(false)

    val taskName = ObservableField<String>("")

    val img = ObservableBoolean(false)

    init {
        hours.set(0)
        minutes.set(0)
        seconds.set(0)
    }

    fun goInfomation(view: View){
        Util.hideSoftKeyboard(view)
        (view.context as MainActivity).supportFragmentManager.beginTransaction()
                    .replace(R.id.main_container, MyInfoFragment())
                    .addToBackStack("MYINFO")
                    .commitAllowingStateLoss()

    }

    fun goHistory(view:View){
        Util.hideSoftKeyboard(view)
        (view.context as MainActivity).supportFragmentManager.beginTransaction()
                .replace(R.id.main_container, HistoryFragment.newInstance())
                .addToBackStack("HISTORY")
                .commitAllowingStateLoss()
    }

    val listener = TextView.OnEditorActionListener { v, actionId, _ ->
        if(actionId == EditorInfo.IME_ACTION_DONE){
            Log.w("MainView", "action done")
            Util.hideSoftKeyboard(v)
            true
        }
        else{
            false
        }
    }

    private var currentSeletedItem = -1

    fun clickHours(view:View){
        isHoursChecked.set(!isHoursChecked.get())
        if(isHoursChecked.get()){
            isMinutesChecked.set(false)
            isSecondsChecked.set(false)
            currentSeletedItem = CurrentStatus.HOURS.status
        }
    }

    fun clickMinutes(view:View){
        isMinutesChecked.set(!isMinutesChecked.get())
        if(isMinutesChecked.get()){
            isHoursChecked.set(false)
            isSecondsChecked.set(false)
            currentSeletedItem = CurrentStatus.MINUTES.status
        }
    }

    fun clickSeconds(view:View){
        isSecondsChecked.set(!isSecondsChecked.get())
        if(isSecondsChecked.get()){
            isHoursChecked.set(false)
            isMinutesChecked.set(false)
            currentSeletedItem = CurrentStatus.SECONDS.status
        }
    }

    fun increaseTime(view:View){
        when(currentSeletedItem){
            CurrentStatus.HOURS.status->{
                if(hours.get() == 23){
                    Toast.makeText(view.context, "Cannot set timer more than one day", Toast.LENGTH_SHORT).show()
                }
                else{
                    hours.set(hours.get()?.plus(1))
                }
            }
            CurrentStatus.MINUTES.status-> {
                if (minutes.get() == 59) {
                    Toast.makeText(view.context, "Adjust your minutes under 59 minutes.", Toast.LENGTH_SHORT).show()
                }
                else {
                    minutes.set(minutes.get()?.plus(1))
                }
            }

            CurrentStatus.SECONDS.status-> {
                if (seconds.get()== 59) {
                    Toast.makeText(view.context, "Adjust your minutes under 59 seconds.", Toast.LENGTH_SHORT).show()
                }
                else {
                    seconds.set(seconds.get()?.plus(1))
                }
            }
        }
    }

    fun decreaseTime(view:View){
        when(currentSeletedItem){
            CurrentStatus.HOURS.status->{
                if(hours.get()!! > 0){
                    hours.set(hours.get()?.minus(1))
                }
            }
            CurrentStatus.MINUTES.status->{
                if(minutes.get()!! > 0){
                    minutes.set(minutes.get()?.minus(1))
                }
            }
            CurrentStatus.SECONDS.status->{
                if(seconds.get()!!> 0){
                    seconds.set(seconds.get()?.minus(1))
                }
            }
        }
    }


    fun triggerTimer(view:View){
        val alarmTime = calculateTimeInMilliSecond(hours.get()!!,
                                                        minutes.get()!!,
                                                        seconds.get()!!)

        Log.w("MainViewModel", "calculated time:: $alarmTime")
        taskName.get()?.let {
            if(it.isEmpty()){
                Toast.makeText(view.context, "MUST set task name", Toast.LENGTH_SHORT).show()
            }
        }
        triggerAlarmWithAlarmManager(view.context, alarmTime)
    }

    private fun triggerAlarmWithAlarmManager(ctx:Context, alarmTime:Long){
        val alarmManager:AlarmManager? = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent:PendingIntent = Intent(ctx, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(ctx, 0 , it,0)
        }
        Log.w("MainViewModel", "onReceive End!!!!!!!!")

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alarmManager?.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + alarmTime,
                    pendingIntent)
        }
        else{
            alarmManager?.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                    SystemClock.elapsedRealtime() + alarmTime,
                    pendingIntent)
        }
    }

    private fun calculateTimeInMilliSecond(hour:Int, minute:Int, second:Int):Long =
            (((60 * 60 * hour) + (60 * minute) + second) * 1000).toLong()
}