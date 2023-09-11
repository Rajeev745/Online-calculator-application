package com.example.onlinesalesmathproject.history

import androidx.lifecycle.LiveData
import com.example.onlinesalesmathproject.history.database.CalculationDao
import com.example.onlinesalesmathproject.history.database.CalculationData
import javax.inject.Inject

class CalculationHistoryRepository @Inject constructor(private val calculationDao: CalculationDao) {

    // For fetching the data
    val allCalculations: LiveData<List<CalculationData>> = calculationDao.getAllCalculations()

    // For fetching the data in order where latest is at the top
    val allCalculationsDesc: LiveData<List<CalculationData>> =
        calculationDao.getDescSubmissionDates()

    /**
     * Method for inserting the data into database.
     *
     * @param calculationData is the object to be saved
     */
    suspend fun insert(calculationData: CalculationData) {
        calculationDao.insert(calculationData)
    }
}