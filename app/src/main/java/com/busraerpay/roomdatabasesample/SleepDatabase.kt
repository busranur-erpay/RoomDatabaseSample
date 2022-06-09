package com.busraerpay.roomdatabasesample

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [SleepNight::class], version = 1, exportSchema = false)
abstract class SleepDatabase : RoomDatabase(){

    abstract fun sleepDatabaseDao(): SleepDatabaseDao

    companion object{
        @Volatile
        private var INSTANCE : SleepDatabase? = null

        fun getInstance(context: Context, scope: CoroutineScope) : SleepDatabase {
                synchronized(this) {
                    //senkrozasyon başlasın
                    var instance = INSTANCE
                //bu değerin null olup olmadığını kontrol etmmeiz lazım
                    if (instance == null){
                        //null ise yeni bir database oluşturcak
                        instance = Room.databaseBuilder(
                            context.applicationContext,
                            SleepDatabase::class.java,
                            "sleep_history_database"
                        )
                            .fallbackToDestructiveMigration()
                            .addCallback(SleepDatabaseCallback(scope))
                            .build()
                        INSTANCE =instance
                    }
                    return  instance
                }
        }

        private class SleepDatabaseCallback(val scope: CoroutineScope): RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                // If you want to keep the data through app restarts,
                // comment out the following line.
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.sleepDatabaseDao())
                    }
                }
            }
        }

        /**
         * Populate the database in a new coroutine.
         * If you want to start with more words, just add them.
         */
        suspend fun populateDatabase(sleepDatabaseDao: SleepDatabaseDao) {
            // Start the app with a clean database every time.
            // Not needed if you only populate on creation.
            sleepDatabaseDao.clear()

            var sleepNight = SleepNight()
            sleepDatabaseDao.insert(sleepNight)
        }

    }
}