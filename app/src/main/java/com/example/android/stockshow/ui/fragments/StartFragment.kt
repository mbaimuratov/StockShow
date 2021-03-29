package com.example.android.stockshow.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.android.stockshow.MainActivity
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.StartFragmentBinding
import com.example.android.stockshow.ui.adapters.RequestsAdapter
import com.example.android.stockshow.ui.adapters.ScreenSlidePagerAdapter
import com.example.android.stockshow.ui.adapters.SearchAdapter
import com.example.android.stockshow.ui.models.SharedViewModel
import com.google.android.material.tabs.TabLayoutMediator

class StartFragment : Fragment(R.layout.start_fragment), RequestsAdapter.OnQueryClickListener,
    SearchAdapter.OnItemClickListener {

    private lateinit var startFragmentBinding: StartFragmentBinding
    private lateinit var searchAdapter: SearchAdapter
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var mSearchView: SearchView

    private var savedQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as MainActivity).supportActionBar?.title = ""
        (activity as MainActivity).supportActionBar?.subtitle = ""

        startFragmentBinding = StartFragmentBinding.bind(view)

        val stocksFragment = StocksFragment()

        val fragmentList = listOf<Fragment>(
            stocksFragment,
            FavouriteFragment()
        )

        startFragmentBinding.viewPager.adapter =
            ScreenSlidePagerAdapter(fragmentList, requireActivity())

        TabLayoutMediator(startFragmentBinding.tabLayout, startFragmentBinding.viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Stocks"
            } else if (position == 1) {
                tab.text = "Favourite"
            }
        }.attach()

        val popularRequestList =
            listOf(
                "Amazon",
                "Intel",
                "Apple",
                "Facebook",
                "Microsoft",
                "Tesla",
                "Caterpillar",
                "Mastercard"
            )
        startFragmentBinding.popularRequestsRv.adapter = RequestsAdapter(popularRequestList, this)
        startFragmentBinding.popularRequestsRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        startFragmentBinding.recentSearchRv.adapter = RequestsAdapter(mutableListOf(), this)
        startFragmentBinding.recentSearchRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        searchAdapter = SearchAdapter(this)
        startFragmentBinding.searchRv.adapter = searchAdapter


        sharedViewModel.searchMap.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it.values.toList())
        }

        setHasOptionsMenu(true)
    }

    override fun onQueryClick(query: String) {
        mSearchView.setQuery(query, false)
    }

    override fun onStarClick(stockItem: StockItem) {
        sharedViewModel.addOrRemoveFavourite(stockItem)
    }

    override fun onItemClick(stockItem: StockItem) {
        val action = StartFragmentDirections.actionStartFragmentToDetailsFragment(stockItem)
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.options_menu, menu)

        val searchMenuItem = menu.findItem(R.id.menu_search)

        mSearchView = searchMenuItem.actionView as SearchView

        mSearchView.onActionViewExpanded()

        searchMenuItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                searchAdapter.filter.filter(savedQuery)

                if (sharedViewModel.stockMap.value != null) {
                    sharedViewModel.setSearchMap(sharedViewModel.stockMap.value!!)
                }

                if (savedQuery.isEmpty()) {
                    startFragmentBinding.preQuerySearchLinearLayout.visibility =
                        View.VISIBLE
                } else {
                    startFragmentBinding.searchRv.visibility =
                        View.VISIBLE
                }

                startFragmentBinding.viewPager.visibility = View.GONE
                startFragmentBinding.tabLayout.visibility = View.GONE

                mSearchView.requestFocus()

                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mSearchView.findFocus(), 0)

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                savedQuery = mSearchView.query.toString()

                startFragmentBinding.preQuerySearchLinearLayout.visibility = View.GONE
                startFragmentBinding.searchRv.visibility = View.GONE
                startFragmentBinding.searchFragmentContainer.visibility =
                    View.GONE
                startFragmentBinding.viewPager.visibility = View.VISIBLE
                startFragmentBinding.tabLayout.visibility = View.VISIBLE

                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                return true
            }
        })

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (startFragmentBinding.recentSearchRv.adapter as RequestsAdapter).appendQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchAdapter.filter.filter(newText)

                if (newText.isBlank()) {
                    if (startFragmentBinding.preQuerySearchLinearLayout.visibility == View.GONE) {
                        startFragmentBinding.preQuerySearchLinearLayout.visibility = View.VISIBLE
                        startFragmentBinding.searchRv.visibility = View.GONE
                    }
                    return false
                }

                if (startFragmentBinding.preQuerySearchLinearLayout.visibility == View.VISIBLE) {
                    startFragmentBinding.preQuerySearchLinearLayout.visibility = View.GONE
                    startFragmentBinding.searchRv.visibility = View.VISIBLE
                }

                return false
            }
        })
    }

    override fun onResume() {
        super.onResume()
        (activity as MainActivity).supportActionBar?.title = ""
        (activity as MainActivity).supportActionBar?.subtitle = ""
    }
}