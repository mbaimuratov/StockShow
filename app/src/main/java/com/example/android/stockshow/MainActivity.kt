package com.example.android.stockshow

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.android.stockshow.databinding.ActivityMainBinding
import com.example.android.stockshow.ui.adapters.ScreenSlidePagerAdapter
import com.example.android.stockshow.ui.fragments.FavouriteFragment
import com.example.android.stockshow.ui.fragments.StocksFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportActionBar != null)
            supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragmentList = arrayListOf(
            StocksFragment(),
            FavouriteFragment()
        )

        binding.viewPager.adapter = ScreenSlidePagerAdapter(fragmentList, this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = "Page ${(position + 1)}"
        }.attach()

        binding.searchView.setOnClickListener {
            binding.searchView.onActionViewExpanded()
            binding.searchFragmentContainer.visibility = View.VISIBLE
            binding.viewPager.visibility = View.GONE
            binding.tabLayout.visibility = View.GONE
        }
    }
}