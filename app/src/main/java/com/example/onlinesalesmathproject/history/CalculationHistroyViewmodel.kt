package com.example.onlinesalesmathproject.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.onlinesalesmathproject.history.database.CalculationData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalculationHistroyViewmodel @Inject constructor(
    private val historyRepository: CalculationHistoryRepository
) : ViewModel() {

    // For fetching the data
    val allCalculations: LiveData<List<CalculationData>> = historyRepository.allCalculations

    // For fetching the data in order where latest is at the top
    val allCalculationsDesc: LiveData<List<CalculationData>> = historyRepository.allCalculationsDesc

    /**
     * Method for inserting the data into database.
     *
     * @param calculationData is the object to be saved
     */
    suspend fun insert(calculationData: CalculationData) {
        historyRepository.insert(calculationData)
    }

}