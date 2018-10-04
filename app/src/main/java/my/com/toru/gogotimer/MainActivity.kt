package my.com.toru.gogotimer

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.os.Message
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.w("MainActivity", "onCreate")
        initView()
    }

    override fun onResume() {
        super.onResume()
        Log.w("MainActivity", "onResume")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.w("MainActivity", "onRestoreInstanceState")
    }

    private var isPlaying = false

    private fun initView(){
        tlb_info.setOnClickListener {
            Toast.makeText(this@MainActivity, "Under Implementation", Toast.LENGTH_SHORT).show()

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
                startService(intent)
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
}