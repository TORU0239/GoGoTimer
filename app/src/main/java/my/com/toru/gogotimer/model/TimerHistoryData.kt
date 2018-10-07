package my.com.toru.gogotimer.model

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "timerHistory")
data class TimerHistoryData(@PrimaryKey(autoGenerate = true) var id:Long?,
                            @ColumnInfo(name = "taskName") var taskName:String,
                            @ColumnInfo(name = "taskStartTimeStamp") var taskStartTimeStamp:Long,
                            @ColumnInfo(name="taskEndTimeStamp") var taskEndTimeStamp:Long,
                            @ColumnInfo(name="lengthOfTimerInMilliSecond") var timerInMillisecond:Long){
    constructor() : this(null, "", 0, 0, 0)
}
