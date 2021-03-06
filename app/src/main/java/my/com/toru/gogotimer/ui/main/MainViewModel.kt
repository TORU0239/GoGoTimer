package my.com.toru.gogotimer.ui.main

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.databinding.ObservableBoolean
import android.databinding.ObservableField
import android.os.Build
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.model.TimerHistoryData
import my.com.toru.gogotimer.service.AlarmReceiver
import my.com.toru.gogotimer.ui.history.HistoryFragment
import my.com.toru.gogotimer.ui.info.MyInfoFragment
import my.com.toru.gogotimer.util.CONST_FINISHED
import my.com.toru.gogotimer.util.CurrentStatus
import my.com.toru.gogotimer.util.SEND_DATA_TASK_NAME
import my.com.toru.gogotimer.util.Util

class MainViewModel{
    companion object {
        private val TAG = MainViewModel::class.java.simpleName
    }

    //region Observable for UI Elements
    var taskNames = ObservableField<String>("")

    val hours = ObservableField<Int>()
    val minutes = ObservableField<Int>()
    val seconds = ObservableField<Int>()

    val isHoursChecked = ObservableBoolean(false)
    val isMinutesChecked = ObservableBoolean(false)
    val isSecondsChecked = ObservableBoolean(false)

    val isAlarmTriggered = ObservableBoolean(false)

    //endregion

    private var currentSelectedItem = -1
    var remainedTime:Long = 0L

    private lateinit var countDownTimer:GoGoCountdownTimer

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

    fun clickHours(v:View) {
        Util.hideSoftKeyboard(v)
        isHoursChecked.set(!isHoursChecked.get())
        if(isHoursChecked.get()){
            isMinutesChecked.set(false)
            isSecondsChecked.set(false)
            currentSelectedItem = CurrentStatus.HOURS.status
        }
    }

    fun clickMinutes(v:View){
        Util.hideSoftKeyboard(v)
        isMinutesChecked.set(!isMinutesChecked.get())
        if(isMinutesChecked.get()){
            isHoursChecked.set(false)
            isSecondsChecked.set(false)
            currentSelectedItem = CurrentStatus.MINUTES.status
        }
    }

    fun clickSeconds(v:View){
        Util.hideSoftKeyboard(v)
        isSecondsChecked.set(!isSecondsChecked.get())
        if(isSecondsChecked.get()){
            isHoursChecked.set(false)
            isMinutesChecked.set(false)
            currentSelectedItem = CurrentStatus.SECONDS.status
        }
    }

    fun increaseTime(view:View){
        when(currentSelectedItem){
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
        when(currentSelectedItem){
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
        if(!isAlarmTriggered.get()){
            taskNames.get()?.let {
                if(it.isEmpty()){
                    Toast.makeText(view.context, "MUST set task name", Toast.LENGTH_SHORT).show()
                }
                else{
                    if(alarmTime > 0){
                        triggerAlarmWithCountdown(view.context, alarmTime)
                    }
                    else{
                        Toast.makeText(view.context, "MUST set timer more than 0 second", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        else{
            Log.w("MainViewModel", "cannot pause!!")
        }
    }

    private fun triggerAlarmWithCountdown(ctx:Context, alarmTime:Long){
        countDownTimer = GoGoCountdownTimer(alarmTime, 1000)
        countDownTimer.start()
        isAlarmTriggered.set(true)
        saveAlarmDataToDB(alarmTime)
    }

    private fun resumeCountdown(alarmTime:Long, nameOfTask:String){
        countDownTimer = GoGoCountdownTimer(alarmTime, 1000)
        countDownTimer.start()
        isAlarmTriggered.set(true)
        taskNames.set(nameOfTask)
    }

    private fun cancelAlarmManager(ctx:Context){
        val alarmManager:AlarmManager? = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val pendingIntent:PendingIntent = Intent(ctx, AlarmReceiver::class.java).let {
            PendingIntent.getBroadcast(ctx, System.currentTimeMillis().toInt() , it,0)
        }
        alarmManager?.cancel(pendingIntent)
    }

    private fun triggerAlarmManager(ctx:Context, alarmTime: Long){
        val pendingIntent:PendingIntent = Intent(ctx, AlarmReceiver::class.java).let {
            it.action = CONST_FINISHED
            it.putExtra(SEND_DATA_TASK_NAME, taskNames.get())
            PendingIntent.getBroadcast(ctx, System.currentTimeMillis().toInt() , it,0)
        }

        val alarmManager:AlarmManager? = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager?.apply {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + alarmTime - 1000,
                        pendingIntent)
            }
            else{
                setExact(AlarmManager.RTC_WAKEUP,
                        System.currentTimeMillis() + alarmTime - 1000,
                        pendingIntent)
            }
        }
        countDownTimer.cancel()
    }

    private fun calculateTimeInMilliSecond(hour:Int, minute:Int, second:Int):Long =
            (((60 * 60 * hour) + (60 * minute) + second) * 1000).toLong()

    private fun saveAlarmDataToDB(alarmTime:Long){
        val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
        val dao = db?.timerHistoryDao()
        val historyData = TimerHistoryData()
        historyData.apply {
            taskNames.get()?.let {
                taskName = it
            }
            taskStartTimeStamp = System.currentTimeMillis()
            timerInMillisecond = alarmTime
        }

        dao?.apply {
            insertData(historyData)
        }
    }

    fun onResume(){
        Log.w("MainView", "onResume")
        val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
        val dao = db?.timerHistoryDao()
        Log.w(TAG, "total count: ${dao?.getTotalCountOfData()}")

        if(dao?.getTotalCountOfData() != 0){
            dao?.getTheLatestOne()?.let {
                Log.w(TAG, "current ID :: ${it.id}, name:: ${it.taskName}")
                if(System.currentTimeMillis() < (it.taskStartTimeStamp + it.timerInMillisecond)){
                    remainedTime = ((it.taskStartTimeStamp + it.timerInMillisecond) - System.currentTimeMillis())
                    Log.w(TAG, "remained Time:: $remainedTime")
                    resumeCountdown(remainedTime, it.taskName)
                    cancelAlarmManager(GoGoTimerApp.applicationContext())
                }
                else{
                    // DO NOTHING, already passed.
                }
            }
        }
    }

    fun onPause(){
        Log.w("MainView", "onPause, remained time:: $remainedTime")
        if(remainedTime != 0L){
            triggerAlarmManager(GoGoTimerApp.applicationContext(), remainedTime)
        }
    }

    inner class GoGoCountdownTimer(private val alarmTime: Long, private val interval:Long):CountDownTimer(alarmTime, interval){
        private fun saveData(){
            val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
            val dao = db?.timerHistoryDao()
            val historyData = TimerHistoryData()
            historyData.apply {
                taskNames.get()?.let {
                    taskName = it
                }
                taskEndTimeStamp = System.currentTimeMillis()
            }
            dao?.insertData(historyData)
        }

        override fun onFinish() {
            isAlarmTriggered.set(false)
            saveData()
        }

        override fun onTick(millisUntilFinished: Long) {
            Log.w("MainViewModel", "remained time:: " + (millisUntilFinished / 1000))
            remainedTime = millisUntilFinished

            val leftHours = (millisUntilFinished / 1000) / 3600
            val leftMinutes = (millisUntilFinished / 1000) / 60
            val leftSeconds = (millisUntilFinished / 1000) % 60

            Log.w("MainViewModel", "hours: $leftHours, minutes: $leftMinutes, seconds: $leftSeconds")

            hours.set(leftHours.toInt())
            minutes.set(leftMinutes.toInt())
            seconds.set(leftSeconds.toInt())
        }
    }
}