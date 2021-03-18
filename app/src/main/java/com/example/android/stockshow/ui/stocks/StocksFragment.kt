package com.example.android.stockshow.ui.stocks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.android.stockshow.R
import com.example.android.stockshow.databinding.StocksFragmentBinding

class StocksFragment : Fragment() {

    private lateinit var viewModel: StocksViewModel
    private lateinit var binding: StocksFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.stocks_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(StocksViewModel::class.java)

        viewModel.fetchStockItems()

        binding = StocksFragmentBinding.bind(view)

        binding.stocksRv.adapter = StocksAdapter()

        val adapter = binding.stocksRv.adapter as StocksAdapter

        viewModel.stockItems.observe(viewLifecycleOwner) { list ->
            adapter.submitList(list)
        }
    }

}