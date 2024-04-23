package com.example.and101_capstone.ui.home

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.and101_capstone.AwardsFragment
import com.example.and101_capstone.QuestsFragment
import com.example.and101_capstone.StatsFragment

class HomeTabAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> StatsFragment()
            1 -> QuestsFragment()
            2 -> AwardsFragment()
            else -> throw IllegalArgumentException("Invalid position")
        }
    }

    override fun getCount(): Int {
        return 3 // Number of tabs
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when (position) {
            0 -> "Stats"
            1 -> "Quests"
            2 -> "Awards"
            else -> null
        }
    }
}
