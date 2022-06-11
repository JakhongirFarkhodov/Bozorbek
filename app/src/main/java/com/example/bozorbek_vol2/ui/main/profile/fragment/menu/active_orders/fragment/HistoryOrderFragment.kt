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
import kotlinx.android.synthetic.main.fragment_history_order.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class HistoryOrderFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var adapter:OrderHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_order, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        observeData()

    }

    override fun onResume() {
        super.onResume()
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

    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null)
            {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { profileViewState ->
                            profileViewState.profileActiveOrHistoryOrder?.let { list ->
                                Log.d(TAG, "profileHistoryOrder: ${list}")
                                if (!list.isEmpty()) {
                                    Log.d(TAG, "profileHistoryOrder dataState: ${list}")
                                    history_order_rv.visibility = View.VISIBLE
                                    viewModel.setProfileAllActiveOrHistoryOrder(list)
                                }
                                else{
                                    history_order_rv.visibility = View.INVISIBLE
                                }
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { viewState ->
            if (viewState != null) {
                viewState.profileActiveOrHistoryOrder?.let { list ->
                    if (!list.isEmpty())
                    {
                        Log.d(TAG, "profileHistoryOrder viewState: ${list}")
                        setListToUi(list)
                    }
                }
            }
        })
    }

    private fun setListToUi(list: List<ProfileActiveOrHistoryOrder>) {
        adapter = OrderHistoryAdapter()
        adapter.submitList(list.distinct().toList())
        history_order_rv.adapter = adapter
        history_order_rv.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)
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