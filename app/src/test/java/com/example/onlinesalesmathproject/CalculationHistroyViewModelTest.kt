package com.example.onlinesalesmathproject

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.onlinesalesmathproject.history.CalculationHistoryRepository
import com.example.onlinesalesmathproject.history.CalculationHistroyViewmodel
import com.example.onlinesalesmathproject.history.database.CalculationDao
import com.example.onlinesalesmathproject.history.database.CalculationData
import com.example.onlinesalesmathproject.history.database.CalculationDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class CalculationHistroyViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()
    private val testScope = TestCoroutineScope(testDispatcher)
    private lateinit var viewModel: CalculationHistroyViewmodel
    private lateinit var historyRepository: CalculationHistoryRepository

    @Mock private lateinit var calculationDao: CalculationDao
    @Mock
    private lateinit var allCalculationsObserver: Observer<List<CalculationData>>
    @Mock
    private lateinit var allCalculationsDescObserver: Observer<List<CalculationData>>

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        calculationDao = mock(CalculationDao::class.java)

        historyRepository = CalculationHistoryRepository(calculationDao)
        viewModel = CalculationHistroyViewmodel(historyRepository)
        viewModel.allCalculations.observeForever(allCalculationsObserver)
        viewModel.allCalculationsDesc.observeForever(allCalculationsDescObserver)

        Dispatchers.setMain(testDispatcher)
    }


    @Test
    fun `test inserting calculation data`() {
        // Prepare data for testing
        val calculationData = CalculationData(0, "2+2=4", 0L, "date")

        // Mock the repository's insert function
        testScope.runBlockingTest {
            doNothing().`when`(historyRepository).insert(calculationData)

            // Call the ViewModel's insert function
            viewModel.insert(calculationData)

            // Verify that the insert function was called
            verify(historyRepository).insert(calculationData)
        }
    }

    @After
    fun cleanup() {
        // Remove observers and reset the dispatcher after the test
        viewModel.allCalculations.removeObserver(allCalculationsObserver)
        viewModel.allCalculationsDesc.removeObserver(allCalculationsDescObserver)
        Dispatchers.resetMain()
        testScope.cleanupTestCoroutines()
    }
}