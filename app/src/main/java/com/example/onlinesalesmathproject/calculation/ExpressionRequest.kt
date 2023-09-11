package com.example.onlinesalesmathproject.calculation

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interface for creating request object for fetching result.
 */
interface ExpressionRequest {

    @GET("v2/query")
    suspend fun evaluateResults(
        @Query("input") input: String,
        @Query("format") format: String = "plaintext",
        @Query("output") output: String = "JSON",
        @Query("appid") appId: String
    ): CalculationResult
}