package my.com.toru.gogotimer.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.databinding.ActivityMainBinding
import my.com.toru.gogotimer.model.TimerHistoryData
import my.com.toru.gogotimer.util.*

class MainActivity : AppCompatActivity(){
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var receiver: TimerReceiver

    private var currentSeletedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val mainBinding: ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        mainBinding.mainViewModel = MainViewModel()
        initView()
        receiver = TimerReceiver()
    }

    override fun onResume() {
        super.onResume()
        Log.w("MainActivity", "onResume")
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction(CONST_UPDATE)
            addAction(CONST_FINISHED)
        }
        registerReceiver(receiver, intentFilter)
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        Log.w(TAG, "onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.w(TAG, "onRestoreInstanceState")
    }

    override fun onPause() {
        unregisterReceiver(receiver)
        super.onPause()
    }

    override fun onBackPressed() {
        if(supportFragmentManager.fragments.size > 0){
            supportFragmentManager.popBackStack()
        }
        else{
            super.onBackPressed()
        }
    }

    private var isPlaying = false

    private fun initView(){
//        btn_trigger_timer.setOnClickListener {
//            if(!isPlaying){
//                Log.w(TAG, "Pushed playing")
//                val alarmTime = calculateTimeInMilliSecond(txt_hours.getInteger(),
//                                                                txt_minutes.getInteger(),
//                                                                txt_seconds.getInteger())
//                Log.w(TAG, "alarmTime::${alarmTime * 1000}")
//
//                if(ed_task.editableText.toString().isEmpty()){
//                    Toast.makeText(this@MainActivity, "MUST set task name", Toast.LENGTH_SHORT).show()
//                }
//                else{
//                    if(alarmTime > 0){
//                        isPlaying = true
//                        btn_trigger_timer.setImageResource(R.drawable.ic_outline_pause_24px)
//
//                        val intent = Intent(this@MainActivity, TimerService::class.java)
//                                        .putExtra("SECOND", alarmTime)
//
//                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                            startForegroundService(intent)
//                        }
//                        else{
//                            startService(intent)
//                        }
//
//                        val db = AppDatabase.getInstance(this@MainActivity)
//                        val dao = db?.timerHistoryDao()
//                        val historyData = TimerHistoryData()
//                        historyData.apply {
//                            taskName = ed_task.editableText.toString()
//                            taskStartTimeStamp = System.currentTimeMillis()
//                        }
//
//                        dao?.apply {
//                            insertData(historyData)
//                            Log.w(TAG, "total Size:: ${getAll().size}")
//                        }
//
//                        txt_hours.isChecked = false
//                        txt_minutes.isChecked = false
//                        txt_seconds.isChecked = false
//                    }
//                    else{
//                        Toast.makeText(this@MainActivity, "MUST set timer more than 0 second", Toast.LENGTH_SHORT)
//                                .show()
//                    }
//                }
//            }
//            else{
//                Log.w("MainActivity", "Pushed not playing")
//                isPlaying = false
//                btn_trigger_timer.setImageResource(R.drawable.ic_outline_arrow_forward_ios_24px)
//                val intent = Intent(this@MainActivity, TimerService::class.java)
//                stopService(intent)
//            }
//        }
    }

    inner class TimerReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            when(intent?.action){
                CONST_UPDATE ->{
                    Log.w(TAG, "update")
                    txt_hours.setFormattedDigit(intent.getIntExtra(CONST_HOURS, -1))
                    txt_minutes.setFormattedDigit(intent.getIntExtra(CONST_MINUTES, -1))
                    txt_seconds.setFormattedDigit(intent.getIntExtra(CONST_SECONDS, -1))
                }
                CONST_FINISHED -> {
                    Log.w(TAG, "finished")
                    btn_trigger_timer.setImageResource(R.drawable.ic_outline_arrow_forward_ios_24px)
                    val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
                    val dao = db?.timerHistoryDao()
                    val historyData = TimerHistoryData()
                    historyData.apply {
                        taskName = ed_task.editableText.toString()
                        taskEndTimeStamp = System.currentTimeMillis()
                    }
                    dao?.insertData(historyData)
                }
                else-> Log.w(TAG, "WTF???")
            }
        }
    }
}