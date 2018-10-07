package my.com.toru.gogotimer.ui.history

import android.view.View
import my.com.toru.gogotimer.app.GoGoTimerApp
import my.com.toru.gogotimer.database.AppDatabase

class HistoryViewModel {

    fun onDeleteAll(view: View){
        val db = AppDatabase.getInstance(view.context)
        db?.timerHistoryDao()?.deleteAllData()
    }

    fun loadDataFromDao(){
        val db = AppDatabase.getInstance(GoGoTimerApp.applicationContext())
        db?.timerHistoryDao()?.getAll()
    }
}