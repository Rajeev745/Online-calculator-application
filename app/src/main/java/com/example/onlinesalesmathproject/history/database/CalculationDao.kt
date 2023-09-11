package com.example.onlinesalesmathproject.history.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CalculationDao {

    @Insert
    suspend fun insert(calculationData: CalculationData)

    @Query("SELECT * FROM calculation_data")
    fun getAllCalculations(): LiveData<List<CalculationData>>

    @Query("SELECT * FROM calculation_data ORDER BY submissionDate DESC")
    fun getDescSubmissionDates(): LiveData<List<CalculationData>>

}