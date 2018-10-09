package my.com.toru.gogotimer.database.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import my.com.toru.gogotimer.model.TimerHistoryData

@Dao
interface TimerHistoryDao {
    @Query("SELECT * from timerHistory order by id DESC")
    fun getAll():List<TimerHistoryData>

    @Insert
    fun insertData(data: TimerHistoryData)

    @Query("DELETE from timerHistory")
    fun deleteAllData()

    @Query("SELECT * from timerHistory order by id DESC LIMIT 1")
    fun getTheLatestOne():TimerHistoryData

    @Query("SELECT COUNT(*) from timerHistory")
    fun getTotalCountOfData():Int
}