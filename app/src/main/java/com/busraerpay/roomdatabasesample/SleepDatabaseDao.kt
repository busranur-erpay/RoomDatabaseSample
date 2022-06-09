package com.busraerpay.roomdatabasesample

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepDatabaseDao {

    @Insert
    suspend fun insert(sleepNight: SleepNight)

    @Update
    fun update(sleepNight: SleepNight)

    @Delete
    fun delete(sleepNight: SleepNight)

    @Query("SELECT * FROM daily_sleep_quality_table WHERE nightId= :key")
    fun get(key : Long) : SleepNight?

    @Query("DELETE FROM daily_sleep_quality_table")
    suspend fun clear()

    //tüm dataları getirme
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC")
    fun getAllNights(): Flow<List<SleepNight>>

    //son geceyi çekme
    @Query("SELECT * FROM daily_sleep_quality_table ORDER BY nightId DESC LIMIT 1")
    fun getTonight() : SleepNight?
}