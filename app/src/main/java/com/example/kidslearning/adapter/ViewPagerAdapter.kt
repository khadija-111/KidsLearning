package com.example.kidslearning.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val fragments: List<Fragment>
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]
}
