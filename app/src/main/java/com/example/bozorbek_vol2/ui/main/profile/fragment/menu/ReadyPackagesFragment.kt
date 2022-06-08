package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.AllReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.MineReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.RecommendedReadyPackagesFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_ready_packages.*


class ReadyPackagesFragment : BaseProfileFragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ready_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = findNavController(this.requireActivity(), R.id.ready_package_nav_host_fragment)

        val navOptions: NavOptions = NavOptions.Builder()
            .setLaunchSingleTop(true)
            .setPopUpTo(navController.graph.startDestinationId, false)
            .build()


        if (profile_ready_packages_tab_layout.selectedTabPosition == 0)
        {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ready_package_nav_host_fragment, AllReadyPackagesFragment()).commit()
        }



        profile_ready_packages_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {

                tab?.let { tab ->



                    when(tab.position)
                    {
                        0 -> {
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ready_package_nav_host_fragment, AllReadyPackagesFragment()).commit()
                        }
                        1 -> {
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ready_package_nav_host_fragment, MineReadyPackagesFragment()).commit()
                        }
                        2 ->{
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ready_package_nav_host_fragment, RecommendedReadyPackagesFragment()).commit()
                        }
                        else ->{
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ready_package_nav_host_fragment, AllReadyPackagesFragment()).commit()
                        }
                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }

        })
    }

}