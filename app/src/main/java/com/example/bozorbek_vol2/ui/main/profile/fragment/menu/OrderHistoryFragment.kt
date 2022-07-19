package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileActiveOrHistoryOrder
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.adapter.OrderHistoryAdapter
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_order_history.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class OrderHistoryFragment : BaseProfileFragment(), OrderHistoryAdapter.OnShowOrdersListener {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var adapter: OrderHistoryAdapter

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
            GlobalScope.launch(Dispatchers.Main) {
                viewModel.setStateEvent(event = ProfileStateEvent.GetAllActiveOrHistoryOrder(
                    UNAPPROVED = "UNAPPROVED",
                    APPROVED = "APPROVED",
                    COLLECTING = "COLLECTING",
                    COLLECTED = "COLLECTED",
                    DELIVERING = "DELIVERING",
                    DELIVERED = null,
                    CANCELLED = null
                ))

            }
        }

        profile_order_history_all_tab_layout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                tab?.let {
                    when(tab.position)
                    {
                        0 ->{
                            GlobalScope.launch(Dispatchers.Main) {
                                viewModel.setStateEvent(event = ProfileStateEvent.GetAllActiveOrHistoryOrder(
                                    UNAPPROVED = "UNAPPROVED",
                                    APPROVED = "APPROVED",
                                    COLLECTING = "COLLECTING",
                                    COLLECTED = "COLLECTED",
                                    DELIVERING = "DELIVERING",
                                    DELIVERED = null,
                                    CANCELLED = null
                                ))

                            }

                        }
                        1->{
                            GlobalScope.launch(Dispatchers.Main) {
                                viewModel.setStateEvent(event = ProfileStateEvent.GetAllActiveOrHistoryOrder(
                                    UNAPPROVED = null,
                                    APPROVED = null,
                                    COLLECTING = null,
                                    COLLECTED = null,
                                    DELIVERING = null,
                                    DELIVERED = "DELIVERED",
                                    CANCELLED = "CANCELLED"
                                ))

                            }
                        }
                        else ->{
                            GlobalScope.launch(Dispatchers.Main) {
                                viewModel.setStateEvent(event = ProfileStateEvent.GetAllActiveOrHistoryOrder(
                                    UNAPPROVED = "UNAPPROVED",
                                    APPROVED = "APPROVED",
                                    COLLECTING = "COLLECTING",
                                    COLLECTED = "COLLECTED",
                                    DELIVERING = "DELIVERING",
                                    DELIVERED = null,
                                    CANCELLED = null
                                ))

                            }
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
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { profileViewState ->
                        profileViewState.profileActiveOrHistoryOrder?.let { list ->
                            Log.d(TAG, "profileActiveOrder: ${list}")
                            if (!list.isEmpty()) {
                                Log.d(TAG, "profileActiveOrder dataState: ${list}")
                                order_history_rv.visibility = View.VISIBLE
                                viewModel.setProfileAllActiveOrHistoryOrder(list)
                            }
                            else{
                                order_history_rv.visibility = View.INVISIBLE
                            }
                        }
                    }
                }
            }
        }
        )

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.profileActiveOrHistoryOrder?.let { list ->
                    if (!list.isEmpty())
                    {
                        Log.d(TAG, "profileActiveOrder viewState: ${list}")
                        setListToUi(list)
                    }
                }
            }
        })
    }

    private fun setListToUi(list: List<ProfileActiveOrHistoryOrder>) {
        Log.d(TAG, "setListToUi: ${list}")
        adapter = OrderHistoryAdapter(this)
        adapter.submitList(list.distinct().toList())
        order_history_rv.adapter = adapter
        order_history_rv.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
    }



    override fun onShowOrder(position: Int, item: ProfileActiveOrHistoryOrder) {
        val action = OrderHistoryFragmentDirections.actionOrderHistoryFragmentToOrdersProcessInfoFragment(position = position, profileActiveOrHistoryOrder = item)
        findNavController().navigate(action)
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