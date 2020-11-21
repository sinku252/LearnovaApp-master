package com.sambhav.tws.base

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class ViewPagerAdapter(
    val manager:FragmentManager,
    val fList:ArrayList<Fragment>,
    val tList:ArrayList<String>
):FragmentStatePagerAdapter(manager) {

    override fun getCount(): Int {
        return fList.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return tList[position]
    }

    override fun getItem(position: Int): Fragment {
        return fList[position]
    }
}