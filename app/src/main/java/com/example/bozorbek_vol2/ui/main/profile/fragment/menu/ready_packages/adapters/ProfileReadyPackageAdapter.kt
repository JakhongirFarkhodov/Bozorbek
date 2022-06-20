package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.AllReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.MineReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.RecommendedReadyPackagesFragment
import dagger.android.support.DaggerAppCompatActivity

class ProfileReadyPackageAdapter(activity:DaggerAppCompatActivity) : FragmentStateAdapter(activity) {
    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position)
        {
            0 ->{
                AllReadyPackagesFragment.newInstance()
            }
            1 ->{
                MineReadyPackagesFragment.newInstance()
            }
            2 ->{
                RecommendedReadyPackagesFragment.newInstance()
            }
            else ->{
                AllReadyPackagesFragment.newInstance()
            }
        }
    }
}