package com.example.android.stockshow.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.FavouriteFragmentBinding
import com.example.android.stockshow.ui.adapters.StocksAdapter
import com.example.android.stockshow.ui.models.SharedViewModel

class FavouriteFragment : Fragment(R.layout.favourite_fragment), StocksAdapter.OnItemClickListener {

    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FavouriteFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FavouriteFragmentBinding.bind(view)

        binding.favouriteRv.adapter = StocksAdapter(this)
        binding.favouriteRv.setHasFixedSize(false)

        sharedViewModel.stockMap.observe(viewLifecycleOwner) {
            val stockList = it.values.toList().filter { stockItem -> stockItem.isFavourite }
            (binding.favouriteRv.adapter as StocksAdapter).submitList(stockList)
        }
    }

    override fun onStarClick(stockItem: StockItem) {
        sharedViewModel.addOrRemoveFavourite(stockItem)
    }

}