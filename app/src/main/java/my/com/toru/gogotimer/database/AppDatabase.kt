package my.com.toru.gogotimer.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import my.com.toru.gogotimer.database.dao.TimerHistoryDao
import my.com.toru.gogotimer.model.TimerHistoryData
import my.com.toru.gogotimer.util.DATABASE_NAME

@Database(entities = [TimerHistoryData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun timerHistoryDao():TimerHistoryDao

    companion object {
        private var INSTANCE:AppDatabase? = null

        fun getInstance(context: Context):AppDatabase?{
            if(INSTANCE == null){
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                                                AppDatabase::class.java,
                                                DATABASE_NAME)
                                                .allowMainThreadQueries()
                                                .build()
            }

            return INSTANCE
        }

        fun releaseInstance(){
            INSTANCE = null
        }
    }
}