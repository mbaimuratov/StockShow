package com.example.android.stockshow.ui.fragments

import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.SearchFragmentBinding
import com.example.android.stockshow.ui.adapters.PopularRequestsAdapter
import com.example.android.stockshow.ui.adapters.StocksAdapter
import com.example.android.stockshow.ui.models.SharedViewModel


class SearchFragment : Fragment(R.layout.search_fragment), StocksAdapter.OnItemClickListener,
    View.OnClickListener {

    private lateinit var binding: SearchFragmentBinding
    private lateinit var stocksAdapter: StocksAdapter

    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = SearchFragmentBinding.bind(view)

        val queryList =
            listOf(
                "Amazon",
                "Bloomberg",
                "Apple",
                "Facebook",
                "Microsoft",
                "Tesla",
                "Alibaba",
                "Mastercard"
            )

        val adapter = PopularRequestsAdapter(queryList, this)
        binding.popularRequestsRv.adapter = adapter

        binding.popularRequestsRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        val queryList2 =
            listOf(
                "Nvidia",
                "Nokia",
                "Yandex",
                "Stripe",
                "Visa",
                "Tesla",
                "Intel",
                "Mastercard"
            )

        binding.recentSearchRv.adapter = PopularRequestsAdapter(queryList2, this)
        binding.recentSearchRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        stocksAdapter = StocksAdapter(this)

        binding.searchRv.adapter = stocksAdapter

        activity?.findViewById<SearchView>(R.id.search_view)
            ?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText.isNullOrBlank()) {
                        if (binding.preQuerySearchLinearLayout.visibility == View.GONE) {
                            binding.preQuerySearchLinearLayout.visibility = View.VISIBLE
                            binding.searchRv.visibility = View.GONE
                        }
                        return false
                    }
                    if (binding.preQuerySearchLinearLayout.visibility == View.VISIBLE) {
                        binding.preQuerySearchLinearLayout.visibility = View.GONE
                        binding.searchRv.visibility = View.VISIBLE
                    }
                    stocksAdapter.filter.filter(newText)
                    return false
                }
            })

        sharedViewModel.stockMap.observe(viewLifecycleOwner) {
            val stockList = it.values.toList()
            stocksAdapter.submitList(stockList)
        }
    }

    override fun onStarClick(stockItem: StockItem) {
        sharedViewModel.addOrRemoveFavourite(stockItem)
    }

    override fun onClick(v: View?) {
        activity?.findViewById<SearchView>(R.id.search_view)
            ?.setQuery((v as TextView).text.toString(), false)
    }

}