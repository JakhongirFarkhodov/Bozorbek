package com.example.bozorbek_vol2.ui.main.profile.fragment.menu

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.ProfileNotification
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.main.profile.adapter.NotificationAdapter
import com.example.bozorbek_vol2.ui.main.profile.fragment.BaseProfileFragment
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import kotlinx.android.synthetic.main.fragment_notification.*


class NotificationFragment : BaseProfileFragment() {

    lateinit var onDataStateChangeListener: OnDataStateChangeListener
    lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notification, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeData()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(event = ProfileStateEvent.SetNotificationEvent())
    }
    private fun observeData() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            onDataStateChangeListener.onDataStateChange(dataState)
            dataState.data?.let { data ->
                data.data?.let { event ->
                    event.getContentIfNotHandled()?.let { profileViewState ->
                        profileViewState.profileNotificationList?.let { list ->
                            if (!list.isEmpty())
                            {
                                Log.d(TAG, "notification dataState: ${list}")
                                viewModel.setNotificationList(list)
                            }
                        }
                    }
                }
            }
        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { profileViewState ->
            profileViewState.profileNotificationList?.let { list ->
                if (!list.isEmpty())
                {
                    setListToUi(list)
                }
            }
        })
    }

    private fun setListToUi(list: List<ProfileNotification>) {
        notificationAdapter = NotificationAdapter()
        notificationAdapter.submitList(list)
        profile_notification_packages_rv.adapter = notificationAdapter
        profile_notification_packages_rv.layoutManager = LinearLayoutManager(this.requireContext(), LinearLayoutManager.VERTICAL, false)

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