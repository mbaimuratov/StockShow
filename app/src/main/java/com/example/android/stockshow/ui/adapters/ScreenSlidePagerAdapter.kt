package com.example.android.stockshow.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(
    private val fragmentList: List<Fragment>,
    activity: FragmentActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) : Fragment {
        return fragmentList[position]
    }
}