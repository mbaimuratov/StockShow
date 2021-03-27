package com.example.android.stockshow

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.android.stockshow.databinding.ActivityMainBinding
import com.example.android.stockshow.ui.adapters.ScreenSlidePagerAdapter
import com.example.android.stockshow.ui.fragments.FavouriteFragment
import com.example.android.stockshow.ui.fragments.StocksFragment
import com.google.android.material.tabs.TabLayoutMediator


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayShowTitleEnabled(false)

        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val fragmentList = arrayListOf<Fragment>(
            StocksFragment(),
            FavouriteFragment()
        )

        binding.viewPager.adapter = ScreenSlidePagerAdapter(fragmentList, this)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0) {
                tab.text = "Stocks"
            } else if (position == 1) {
                tab.text = "Favourite"
            }
        }.attach()
    }
}