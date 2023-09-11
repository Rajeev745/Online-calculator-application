package com.example.onlinesalesmathproject.calculation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinesalesmathproject.R
import com.example.onlinesalesmathproject.databinding.FragmentCalculationBinding
import com.example.onlinesalesmathproject.history.CalculationHistroyViewmodel
import com.example.onlinesalesmathproject.history.database.CalculationData
import com.example.onlinesalesmathproject.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class CalculationFragment : Fragment() {

    private val TAG = "CalculationFragment"

    var binding: FragmentCalculationBinding? = null

    val calculationViewmodel by viewModels<CalculationViewmodel>()
    val histroyViewmodel by viewModels<CalculationHistroyViewmodel>()
    private val calculationRecyclerViewAdapter by lazy {
        CalculationRecyclerViewAdapter()
    }

    val inputList: MutableList<String> = ArrayList()
    private val resultList: MutableList<String> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalculationBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        // Naviagting to history fragment
        binding?.historyButton?.setOnClickListener {
            findNavController().navigate(R.id.action_calculationFragment_to_historyFragment)
        }

        // Fetching the data from the API since API didn't supported multiple questions at once
        // so I used for loop for every request
        binding?.submitButton?.setOnClickListener {
            getExpression()
            for (list in inputList) {
                calculationViewmodel.getResultData(list)
            }
            inputList.clear()
        }

        // Observing the data from the flow
        lifecycleScope.launch {
            calculationViewmodel.getResult.collect() {
                when (it) {
                    is Resource.Success -> {
                        Log.d(TAG, it.data?.pods?.get(1)?.subpods?.get(0)?.plaintext.toString())
                        // Creating the data for [CalculationData]
                        val output =
                            "${it.data?.inputstring} => ${it.data?.pods?.get(1)?.subpods?.get(0)?.plaintext}"
                        val totalTime: Long = getTodaysDateMillis()
                        val todaysDate = getTodaysDate()

                        val resultData = CalculationData(
                            calculationString = output,
                            submissionDate = totalTime,
                            dateInString = todaysDate
                        )

                        // Saving the data to the database
                        histroyViewmodel.insert(resultData)
                        resultList.add(output)

                        // Sending the data to recycler view
                        calculationRecyclerViewAdapter.differ.submitList(resultList)
                        calculationRecyclerViewAdapter.notifyDataSetChanged()
                    }
                    is Resource.Error -> {
                        Log.d(TAG, it.message.toString())
                        Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                    is Resource.Loading -> {
                        //TODO: Set up a progress bar
                    }
                    else -> Unit
                }
            }
        }
    }

    /**
     * Getting the time in long format for getting fetching the data into descending order
     */
    private fun getTodaysDateMillis(): Long {
        return System.currentTimeMillis()
    }

    /**
     * Getting submission date in string format
     */
    private fun getTodaysDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd") // Change the format as needed
        return dateFormat.format(calendar.time)
    }

    /**
     * Setting up the recycler view. */
    private fun setUpRecyclerView() {
        binding?.calculationRecyclerView?.apply {
            adapter = calculationRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    /**
     * Getting the value from the edit Text
     *
     * Note:- For getting result you have to enter each value in different line that is why
     * split lines are used.
     */
    fun getExpression() {
        val expressions = binding?.inputEditText?.text.toString()
        val inputLines = expressions.split("\n")
        for (line in inputLines) {
            if (isValidMathExpression(line)) {
                inputList.add(line)
            }
        }
        binding?.inputEditText?.text?.clear()
    }

    /**
     * Checking the validation only numbers and following operations are supported now:
     * Division, Multiplication, addition, subtraction, modulus and power
     */
    fun isValidMathExpression(input: String): Boolean {
        val pattern = Regex("^[\\d+\\-*/^%]+$")
        return pattern.matches(input)
    }

    /**
     * Setting the binding to null to avoid any data leakage
     */
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

}