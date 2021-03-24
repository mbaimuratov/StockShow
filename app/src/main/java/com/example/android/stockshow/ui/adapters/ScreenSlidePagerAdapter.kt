package com.example.android.stockshow.ui.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ScreenSlidePagerAdapter(
    private val fragmentList: ArrayList<Fragment>,
    activity: AppCompatActivity
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = fragmentList.size

    override fun createFragment(position: Int) = fragmentList[position]
}