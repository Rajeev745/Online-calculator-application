package com.example.onlinesalesmathproject.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.onlinesalesmathproject.R
import com.example.onlinesalesmathproject.databinding.FragmentHistoryBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private var binding: FragmentHistoryBinding? = null
    private val historyRecyclerViewAdapter by lazy {
        HistoryRecyclerViewAdapter()
    }
    private val viewmodel by viewModels<CalculationHistroyViewmodel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()

        binding?.backButton?.setOnClickListener {
            findNavController().navigate(R.id.action_historyFragment_to_calculationFragment)
        }

        // Fetching data in history fragment
        viewmodel.allCalculations.observe(viewLifecycleOwner) { calculationData ->
            historyRecyclerViewAdapter.differ.submitList(calculationData)
        }

        // For fetching the data as latest on the top
        binding?.fetchDataButton?.setOnClickListener {
            viewmodel.allCalculationsDesc.observe(viewLifecycleOwner) {
                historyRecyclerViewAdapter.differ.submitList(it)
            }
        }
    }

    // Setting up the recycler view
    private fun setUpRecyclerView() {
        binding?.historyRecyclerView?.apply {
            adapter = historyRecyclerViewAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    // Setting the binding to null for avoiding leakage
    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}