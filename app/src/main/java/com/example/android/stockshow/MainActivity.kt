package com.example.android.stockshow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.android.stockshow.databinding.ActivityMainBinding
import com.example.android.stockshow.ui.ScreenSlidePagerAdapter
import com.example.android.stockshow.ui.favourite.FavouriteFragment
import com.example.android.stockshow.ui.stocks.StocksFragment
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}