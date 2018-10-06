package my.com.toru.gogotimer.ui.history

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import my.com.toru.gogotimer.R

class HistoryActivity : AppCompatActivity() {

    companion object {
        private val TAG = HistoryActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)
    }
}