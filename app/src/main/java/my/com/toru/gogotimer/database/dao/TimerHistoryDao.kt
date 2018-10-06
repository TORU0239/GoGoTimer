package my.com.toru.gogotimer.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import my.com.toru.gogotimer.model.TimerHistoryData

@Dao
interface TimerHistoryDao {
    @Query("SELECT * from timerHistory")
    fun getAll():List<TimerHistoryData>

    @Insert
    fun insertData(data: TimerHistoryData)
}