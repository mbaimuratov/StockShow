package com.example.android.stockshow.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.FavouriteFragmentBinding
import com.example.android.stockshow.ui.adapters.FavouriteAdapter
import com.example.android.stockshow.ui.models.FavouriteViewModel
import com.example.android.stockshow.ui.models.SharedViewModel

class FavouriteFragment : Fragment(), FavouriteAdapter.OnItemClickListener {

    private lateinit var viewModel: FavouriteViewModel
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private lateinit var binding: FavouriteFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favourite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(FavouriteViewModel::class.java)

        binding = FavouriteFragmentBinding.bind(view)
        binding.favouriteRv.adapter = FavouriteAdapter(this)
        binding.favouriteRv.setHasFixedSize(false)

        val adapter = binding.favouriteRv.adapter as FavouriteAdapter

        sharedViewModel.stockMap.observe(viewLifecycleOwner) {
            val stockList = it.values.toList().filter { stockItem -> stockItem.isFavourite }
            adapter.submitList(stockList)
        }
    }

    override fun onStarClick(stockItem: StockItem) {
        sharedViewModel.addOrRemoveFavourite(stockItem)
    }

}