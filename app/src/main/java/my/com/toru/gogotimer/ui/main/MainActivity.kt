package my.com.toru.gogotimer.ui.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.model.TimerHistoryData
import my.com.toru.gogotimer.service.TimerService
import my.com.toru.gogotimer.ui.history.HistoryActivity

class MainActivity : AppCompatActivity() {
    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    private lateinit var receiver: TestBroadcastReceiver

    private var currentSeletedItem = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.w("MainActivity", "onCreate")
        initView()
        receiver = TestBroadcastReceiver()
    }

    override fun onResume() {
        super.onResume()
        Log.w("MainActivity", "onResume")
        val intentFilter = IntentFilter()
        intentFilter.apply {
            addAction("com.my.toru.UPDATE")
            addAction("com.my.toru.FINISHED")
        }
        registerReceiver(receiver, intentFilter)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.w(TAG, "onRestoreInstanceState")
    }

    override fun onPause() {
        unregisterReceiver(receiver)
        super.onPause()
    }

    private var isPlaying = false

    private fun initView(){
        tlb_info.setOnClickListener {

            val dialog = AlertDialog.Builder(this@MainActivity)
            dialog.setTitle("User Info")
                    .setMessage("Created by Toru Wonyoung Choi")
                    .setNeutralButton("Dismiss") { dialogInterface, p1 ->
                        dialogInterface.dismiss()
                    }
                    .create().show()
        }

        tlb_history.setOnClickListener {
            startActivity(Intent(this@MainActivity, HistoryActivity::class.java))
        }

        btn_increase_time.setOnClickListener {
            when(currentSeletedItem){
                CurrentStatus.HOURS.status->{
                    if((txt_hours.text as String).toInt() == 23){
                        Toast.makeText(this@MainActivity, "Cannot set timer more than one day", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        txt_hours.setFormattedDigit((Integer.parseInt(txt_hours.text as String) + 1))
                    }
                }
                CurrentStatus.MINUTES.status->{
                    if((txt_minutes.text as String).toInt() == 59){
                        txt_minutes.setFormattedDigit(0)
                        txt_hours.setFormattedDigit((Integer.parseInt(txt_minutes.text as String) + 1))
                    }
                    else{
                        txt_minutes.setFormattedDigit((Integer.parseInt(txt_minutes.text as String) + 1))
                    }
                }
                CurrentStatus.SECONDS.status->{
                    if((txt_seconds.text as String).toInt() == 59){
                        txt_seconds.setFormattedDigit(0)
                        txt_minutes.setFormattedDigit((Integer.parseInt(txt_minutes.text as String) + 1))
                    }
                    else{
                        txt_seconds.setFormattedDigit((Integer.parseInt(txt_seconds.text as String) + 1))
                    }
                }
            }
        }

        btn_decrease_time.setOnClickListener {
            when(currentSeletedItem){
                CurrentStatus.HOURS.status->{
                    if((txt_hours.text as String).toInt() > 0){
                        txt_hours.setFormattedDigit((Integer.parseInt(txt_hours.text as String) - 1))
                    }
                }
                CurrentStatus.MINUTES.status->{
                    if((txt_minutes.text as String).toInt() > 0){
                        txt_minutes.setFormattedDigit((Integer.parseInt(txt_minutes.text as String) - 1))
                    }
                }
                CurrentStatus.SECONDS.status->{
                    if((txt_seconds.text as String).toInt() > 0){
                        txt_seconds.setFormattedDigit((Integer.parseInt(txt_seconds.text as String) - 1))
                    }
                }
            }
        }

        ed_task.apply{
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(e: Editable?) {}

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
            setOnEditorActionListener { p0, p1, p2 -> true}
        }

        btn_trigger_timer.setOnClickListener {
            if(!isPlaying){
                Log.w(TAG, "Pushed playing")
                val alarmTime = calculateTimeInMilliSecond(txt_hours.getInteger(),
                                                                txt_minutes.getInteger(),
                                                                txt_seconds.getInteger())
                Log.w(TAG, "alarmTime::${alarmTime * 1000}")

                if(alarmTime > 0){
                    isPlaying = true
                    btn_trigger_timer.setImageResource(R.drawable.ic_outline_pause_24px)

                    val intent = Intent(this@MainActivity, TimerService::class.java)
                    intent.putExtra("SECOND", alarmTime)
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        startForegroundService(intent)
                    }
                    else{
                        startService(intent)
                    }

                    val db = AppDatabase.getInstance(this@MainActivity)
                    val dao = db?.timerHistoryDao()
                    val historyData = TimerHistoryData()
                    historyData.apply {
                        taskName = ed_task.editableText.toString()
                        taskStartTimeStamp = System.currentTimeMillis()
                    }

                    dao?.apply {
                        insertData(historyData)
                        Log.w(TAG, "total Size:: ${getAll().size}")
                    }


                    txt_hours.isChecked = false
                    txt_minutes.isChecked = false
                    txt_seconds.isChecked = false
                }
                else{
                    Toast.makeText(this@MainActivity, "MUST set timer more than 0 second", Toast.LENGTH_SHORT)
                            .show()
                }
            }
            else{
                Log.w("MainActivity", "Pushed not playing")
                isPlaying = false
                btn_trigger_timer.setImageResource(R.drawable.ic_outline_arrow_forward_ios_24px)
                val intent = Intent(this@MainActivity, TimerService::class.java)
                stopService(intent)
            }
        }

        txt_hours.setOnClickListener {
            txt_hours.isChecked = !txt_hours.isChecked
            if(txt_hours.isChecked){
                txt_minutes.isChecked = false
                txt_seconds.isChecked = false
                currentSeletedItem = CurrentStatus.HOURS.status
            }
        }

        txt_minutes.setOnClickListener {
            txt_minutes.isChecked = !txt_minutes.isChecked
            if(txt_minutes.isChecked){
                txt_hours.isChecked = false
                txt_seconds.isChecked = false
                currentSeletedItem = CurrentStatus.MINUTES.status
            }
        }

        txt_seconds.setOnClickListener {
            txt_seconds.isChecked = !txt_seconds.isChecked
            if(txt_seconds.isChecked){
                txt_hours.isChecked = false
                txt_minutes.isChecked = false
                currentSeletedItem = CurrentStatus.SECONDS.status
            }
        }
    }

    private fun TextView.setFormattedDigit(digit:Int){
        this.text = String.format("%1$02d", digit)
    }

    // TODO:
    private fun calculateTimeInMilliSecond(hour:Int, minute:Int, second:Int):Int =
            ((60 * 60 * hour) + (60 * minute) + second) * 1000

    private fun TextView.getInteger():Int = Integer.parseInt(this.text as String)

    inner class TestBroadcastReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.w("MainActivity", "update")
            when(intent?.action){
                "com.my.toru.UPDATE"->{
                    Log.w(TAG, "update")
                    txt_seconds.setFormattedDigit(intent.getIntExtra("UPDATE", -1))
                }
                "com.my.toru.FINISHED"->{
                    btn_trigger_timer.setImageResource(R.drawable.ic_outline_arrow_forward_ios_24px)
                    val db = AppDatabase.getInstance(this@MainActivity)
                    val dao = db?.timerHistoryDao()
                    val historyData = TimerHistoryData()
                    historyData.apply {
                        taskName = ed_task.editableText.toString()
                        taskEndTimeStamp = System.currentTimeMillis()
                    }

                    dao?.let{
                        it.insertData(historyData)
                        Log.w(TAG, "total Size:: ${it.getAll().size}")
                    }

                }
                else->{
                    Log.w(TAG, "WTF???")
                }
            }
        }
    }
}

enum class CurrentStatus(val status:Int){
    HOURS(1),
    MINUTES(2),
    SECONDS(3)
}