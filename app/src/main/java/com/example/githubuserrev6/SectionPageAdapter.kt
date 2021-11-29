package com.example.githubuserrev6

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class SectionPageAdapter(activity: AppCompatActivity, bundle: Bundle) :
    FragmentStateAdapter(activity) {
    private val bundleFragment = bundle

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        var allFragment: Fragment? = null

        when (position) {
            0 -> allFragment = FollowersFragment()
            1 -> allFragment = FollowingFragment()
            2 -> allFragment = RepositoryFragment()
        }

        allFragment?.arguments = this.bundleFragment
        return allFragment as Fragment
    }

}