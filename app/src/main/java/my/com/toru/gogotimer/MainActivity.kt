package my.com.toru.gogotimer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var receiver:TestBroadcastReceiver

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
        LocalBroadcastManager.getInstance(this@MainActivity).registerReceiver(receiver, IntentFilter("com.my.toru.UPDATE"))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.w("MainActivity", "onRestoreInstanceState")
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this@MainActivity).unregisterReceiver(receiver)
        super.onDestroy()
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
            Toast.makeText(this@MainActivity, "Under Construction", Toast.LENGTH_SHORT).show()
        }

        btn_increase_time.setOnClickListener {

        }

        btn_decrease_time.setOnClickListener {

        }

        ed_task.apply{
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(e: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
            })
            setOnEditorActionListener { p0, p1, p2 -> true}
        }

        btn_trigger_timer.setOnClickListener {
            if(!isPlaying){
                Log.w("MainActivity", "Pushed playing")
                isPlaying = true
                btn_trigger_timer.setImageResource(R.drawable.ic_outline_pause_24px)
                val intent = Intent(this@MainActivity, TimerService::class.java)

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    startForegroundService(intent)
                }
                else{
                    startService(intent)
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

        }

        txt_minutes.setOnClickListener {

        }

        txt_seconds.setOnClickListener {

        }
    }

    inner class TestBroadcastReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.w("MainActivity", "update")
            when(intent?.action){
                "com.my.toru.UPDATE"->{
                    Log.w("MainActivity", "update")
                    txt_seconds.text = intent.getIntExtra("UPDATE", -1).toString()
                }
                else->{
                    Log.w("MainActivity", "WTF???")
                }
            }
        }

    }
}