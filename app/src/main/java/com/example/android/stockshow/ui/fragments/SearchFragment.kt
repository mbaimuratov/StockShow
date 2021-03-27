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
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.android.stockshow.R
import com.example.android.stockshow.data.response.StockItem
import com.example.android.stockshow.databinding.SearchFragmentBinding
import com.example.android.stockshow.ui.adapters.RequestsAdapter
import com.example.android.stockshow.ui.adapters.SearchAdapter
import com.example.android.stockshow.ui.models.SharedViewModel
import com.google.android.material.tabs.TabLayout


class SearchFragment : Fragment(R.layout.search_fragment), RequestsAdapter.OnQueryClickListener,
    SearchAdapter.OnItemClickListener {

    private lateinit var searchFragmentBinding: SearchFragmentBinding
    private lateinit var searchAdapter: SearchAdapter
    private lateinit var mSearchView: SearchView
    private val sharedViewModel: SharedViewModel by activityViewModels()

    private var savedQuery = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchFragmentBinding = SearchFragmentBinding.bind(view)

        val popularRequestList =
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
        searchFragmentBinding.popularRequestsRv.adapter = RequestsAdapter(popularRequestList, this)
        searchFragmentBinding.popularRequestsRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        searchFragmentBinding.recentSearchRv.adapter = RequestsAdapter(mutableListOf(), this)
        searchFragmentBinding.recentSearchRv.layoutManager =
            StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL)

        searchAdapter = SearchAdapter(this)
        searchFragmentBinding.searchRv.adapter = searchAdapter


        sharedViewModel.searchMap.observe(viewLifecycleOwner) {
            searchAdapter.submitList(it.values.toList())
        }

        setHasOptionsMenu(true)
    }

    override fun onStarClick(stockItem: StockItem) {
        sharedViewModel.addOrRemoveFavourite(stockItem)
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

                activity?.findViewById<FragmentContainerView>(R.id.search_fragment_container)?.visibility =
                    View.VISIBLE
                activity?.findViewById<ViewPager2>(R.id.view_pager)?.visibility = View.GONE
                activity?.findViewById<TabLayout>(R.id.tab_layout)?.visibility = View.GONE

                mSearchView.requestFocus()
                //open kb
                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(mSearchView.findFocus(), 0)

                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                savedQuery = mSearchView.query.toString()

                activity?.findViewById<FragmentContainerView>(R.id.search_fragment_container)?.visibility =
                    View.GONE
                activity?.findViewById<ViewPager2>(R.id.view_pager)?.visibility = View.VISIBLE
                activity?.findViewById<TabLayout>(R.id.tab_layout)?.visibility = View.VISIBLE

                //hide kb
                val imm =
                    activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                return true
            }
        })

        mSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (searchFragmentBinding.recentSearchRv.adapter as RequestsAdapter).appendQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchAdapter.filter.filter(newText)
                
                if (newText.isBlank()) {
                    if (searchFragmentBinding.preQuerySearchLinearLayout.visibility == View.GONE) {
                        searchFragmentBinding.preQuerySearchLinearLayout.visibility = View.VISIBLE
                        searchFragmentBinding.searchRv.visibility = View.GONE
                    }
                    return false
                }

                if (searchFragmentBinding.preQuerySearchLinearLayout.visibility == View.VISIBLE) {
                    searchFragmentBinding.preQuerySearchLinearLayout.visibility = View.GONE
                    searchFragmentBinding.searchRv.visibility = View.VISIBLE
                }

                return false
            }
        })
    }

    override fun onQueryClick(query: String) {
        mSearchView.setQuery(query, false)
    }
}