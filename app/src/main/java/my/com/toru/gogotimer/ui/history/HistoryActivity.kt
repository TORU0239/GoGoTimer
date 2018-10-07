package my.com.toru.gogotimer.ui.history

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_history.*
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.model.TimerHistoryData
import java.util.*

class HistoryActivity : AppCompatActivity() {

    companion object {
        private val TAG = HistoryActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
        val db = AppDatabase.getInstance(this@HistoryActivity)
        val totalDB = db?.timerHistoryDao()?.getAll()
        rcvTimerHistory.adapter = HistoryAdapter(totalDB as ArrayList<TimerHistoryData>)
    }
}