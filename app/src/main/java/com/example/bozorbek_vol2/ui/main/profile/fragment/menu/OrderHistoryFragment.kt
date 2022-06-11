package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.fragment.ActiveOrderFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.fragment.HistoryOrderFragment
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_order_history.*


class OrderHistoryFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_order_history, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (profile_order_history_all_tab_layout.selectedTabPosition == 0)
        {
            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.order_history_nav_host_fragment, ActiveOrderFragment()).commit()
        }

        profile_order_history_all_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when(tab.position)
                    {
                        0 ->{
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.order_history_nav_host_fragment, ActiveOrderFragment()).commit()

                        }
                        1->{
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.order_history_nav_host_fragment, HistoryOrderFragment()).commit()
                        }
                        else ->{
                            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.order_history_nav_host_fragment, ActiveOrderFragment()).commit()


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




    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        }
        catch (e:Exception)
        {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }
    }

}