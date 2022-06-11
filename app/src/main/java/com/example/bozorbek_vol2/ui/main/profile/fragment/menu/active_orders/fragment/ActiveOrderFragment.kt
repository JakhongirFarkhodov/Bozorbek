package com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileActiveOrHistoryOrder
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.adapter.OrderHistoryAdapter
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import kotlinx.android.synthetic.main.fragment_active_order.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ActiveOrderFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var adapter:OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_active_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        GlobalScope.launch(Main) {
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
                                    active_order_rv.visibility = View.VISIBLE
                                    viewModel.setProfileAllActiveOrHistoryOrder(list)
                                }
                                else{
                                    active_order_rv.visibility = View.INVISIBLE
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
        adapter = OrderHistoryAdapter()
        adapter.submitList(list.distinct().toList())
        active_order_rv.adapter = adapter
        active_order_rv.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
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