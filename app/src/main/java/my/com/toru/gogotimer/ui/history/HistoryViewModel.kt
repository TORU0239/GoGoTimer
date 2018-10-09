package my.com.toru.gogotimer.ui.history

import android.view.View
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase
import my.com.toru.gogotimer.model.TimerHistoryData
import java.util.concurrent.Executors

class HistoryViewModel {

    fun onDeleteAll(view: View){
        val db = AppDatabase.getInstance(view.context)
        db?.timerHistoryDao()?.deleteAllData()
    }

    fun loadDataFromDao(callback:(List<TimerHistoryData>)->Unit){
        val executorService = Executors.newSingleThreadExecutor()
        with(executorService){
            execute{
                val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
                db?.timerHistoryDao()?.getAll()?.let {
                    callback(it)
                }
            }
        }
    }
}