package my.com.toru.gogotimer.ui.history

import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.activity_history.*
import my.com.toru.gogotimer.BR
import my.com.toru.gogotimer.R
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.databinding.ActivityHistoryBinding
import my.com.toru.gogotimer.model.TimerHistoryData
import java.util.*

class HistoryActivity : AppCompatActivity() {

    companion object {
        private val TAG = HistoryActivity::class.java.simpleName
    }

    private lateinit var historyDataBinding:ActivityHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        historyDataBinding = DataBindingUtil.setContentView(this@HistoryActivity, R.layout.activity_history)
        historyDataBinding.historyViewModel = HistoryViewModel()
        initRecyclerView()
    }

    private fun initRecyclerView(){
        val db = AppDatabase.getInstance(this@HistoryActivity)
        val totalDB = db?.timerHistoryDao()?.getAll()
        rcvTimerHistory.adapter = HistoryAdapter(totalDB as ArrayList<TimerHistoryData>)
        rcvTimerHistory.addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.VERTICAL))
    }
}