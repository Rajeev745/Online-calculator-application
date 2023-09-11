package com.example.onlinesalesmathproject

import com.example.onlinesalesmathproject.calculation.CalculationFragment
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class CalculationFragmentTest {

    private lateinit var fragment: CalculationFragment

    @Before
    fun setUp() {
        fragment = CalculationFragment()
    }

    @Test
    fun `check for valid expression`() {
        val expression = "2+2"
        val result = fragment.isValidMathExpression(expression)
        Assert.assertTrue(result)
    }

    @Test
    fun `check for non valid expression`() {
        val expression = "a+b"
        val result = fragment.isValidMathExpression(expression)
        Assert.assertFalse(result)
    }

}