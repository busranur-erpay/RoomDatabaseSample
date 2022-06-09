package com.busraerpay.roomdatabasesample

import androidx.annotation.WorkerThread
import kotlinx.coroutines.flow.Flow

class SleepRepository(private  val sleepDatabaseDao: SleepDatabaseDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    //tüm geceleri çekiyoruz
    val allSleepNights : Flow<List<SleepNight>> = sleepDatabaseDao.getAllNights()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(sleepNight: SleepNight) {
        sleepDatabaseDao.insert(sleepNight)
    }
}