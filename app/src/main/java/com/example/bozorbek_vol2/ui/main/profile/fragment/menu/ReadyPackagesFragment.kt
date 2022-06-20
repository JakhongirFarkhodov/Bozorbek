package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.AllReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.MineReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.RecommendedReadyPackagesFragment
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_ready_packages.*


class ReadyPackagesFragment : BaseProfileFragment() {


    lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ready_packages, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        if (profile_order_history_tab_layout.selectedTabPosition == 0)
        {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.ready_package_nav_host_fragment, AllReadyPackagesFragment()).commit()
        }



        profile_order_history_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
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

        observeData()
    }



    private fun observeData() {
       val id =  viewModel.sharedPreferences.getInt("id", 0)
        if (id != 0)
        {
            viewModel.setStateEvent(event = ProfileStateEvent.SetReadyPackageId(id))
            viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { profileViewState ->
                            profileViewState.profileReadyPackageIdList?.let { list ->
                                if (!list.isEmpty())
                                {
                                    Log.d(TAG, "profileReadyPackageIdList dataState: ${list}")
                                    viewModel.setProfileReadyPackageIdList(list)
                                }
                            }
                        }
                    }
                }
            })

            viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
                viewState.profileReadyPackageIdList?.let { list ->
                    if (!list.isEmpty())
                    {
                        Log.d(TAG, "profileReadyPackageIdList viewState: ${list}")
                    }
                }
            })
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        }
        catch (e:Exception)
        {
            Log.d(TAG, "onAttach: ${context} must implement onDataStateChangeListener")
        }
    }

}