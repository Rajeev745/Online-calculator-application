package com.example.onlinesalesmathproject

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.onlinesalesmathproject.history.database.CalculationDao
import com.example.onlinesalesmathproject.history.database.CalculationData
import com.example.onlinesalesmathproject.history.database.CalculationDatabase
import com.example.onlinesalesmathproject.history.database.getOrAwaitValue
import kotlinx.coroutines.runBlocking
import org.junit.*

class CalculationDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    lateinit var calculationDao: CalculationDao
    lateinit var calculationDatabase: CalculationDatabase

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        calculationDatabase = Room.inMemoryDatabaseBuilder(
            context, CalculationDatabase::class.java
        ).allowMainThreadQueries().build()
        calculationDao = calculationDatabase.calculationDao()
    }

    @Test
    fun `insert calculation expected single calculation`() = runBlocking {
        val calculation = CalculationData(0, "2+2", 0, "2-2-2022")
        calculationDao.insert(calculation)

        val result = calculationDao.getAllCalculations().getOrAwaitValue()

        Assert.assertEquals(1, result.size)
        Assert.assertEquals("2+2", result[0].calculationString)
    }

    @After
    fun tearDown() {
        calculationDatabase.close()
    }
}