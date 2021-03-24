package com.example.android.stockshow.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.android.stockshow.App
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.StocksFragmentBinding
import com.example.android.stockshow.ui.adapters.StocksAdapter
import com.example.android.stockshow.ui.factories.StocksViewModelFactory
import com.example.android.stockshow.ui.models.SharedViewModel
import com.example.android.stockshow.ui.models.StocksViewModel

class StocksFragment : Fragment(R.layout.stocks_fragment), StocksAdapter.OnItemClickListener {

    private lateinit var viewModel: StocksViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var binding: StocksFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = StocksFragmentBinding.bind(view)
        binding.stocksRv.adapter = StocksAdapter(this)
        binding.stocksRv.setHasFixedSize(false)

        val adapter = binding.stocksRv.adapter as StocksAdapter

        initVm()

        viewModel.stockMap.observe(viewLifecycleOwner) { map ->
            sharedViewModel.setStockMap(map)
        }

        sharedViewModel.stockMap.observe(viewLifecycleOwner) {
            val stockList = it.values.toList()
            adapter.submitList(stockList)
            adapter.notifyDataSetChanged()
        }

        viewModel.setupStockMap()
    }

    private fun initVm() {
        val app = requireActivity().application as App
        val repo = app.repository
        val factory = StocksViewModelFactory(repo)
        viewModel = ViewModelProvider(this, factory).get(StocksViewModel::class.java)
    }

    override fun onStarClick(stockItem: StockItem) {
        sharedViewModel.addOrRemoveFavourite(stockItem)
    }
}