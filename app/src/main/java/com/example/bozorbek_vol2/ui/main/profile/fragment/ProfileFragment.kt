package com.example.bozorbek_vol2.ui.main.profile.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : BaseProfileFragment() {

    private var showMenu:Boolean = false
    private lateinit var onDataStateChangeListener: OnDataStateChangeListener

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        mb_go_to_registration.setOnClickListener {
            val intent = Intent(requireActivity(), AuthActivity::class.java)
            startActivity(intent)
        }

        observeData()
        getProfileInfo()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setStateEvent(event = ProfileStateEvent.GetProfileInfo())
    }


    private fun observeData() {

        Log.d(TAG, "observeData: ${viewModel.sessionManager.cachedAuthToken.value}")
        val checkUser = viewModel.sessionManager.cachedAuthToken.value

        if (checkUser != null)
        {
            showMenu = true
            lottie_constraint.visibility = View.GONE
            profile_constraint_layout.visibility = View.VISIBLE
            viewModel.setStateEvent(event = ProfileStateEvent.GetProfileInfo())

        }
        else{
            showMenu = false
            lottie_constraint.visibility = View.VISIBLE
            profile_constraint_layout.visibility = View.GONE
        }

    }

    private fun getProfileInfo() {
        viewModel.dataState.observe(viewLifecycleOwner, Observer { dataState ->
            if (dataState != null)
            {
                onDataStateChangeListener.onDataStateChange(dataState)
                dataState.data?.let { data ->
                    data.data?.let { event ->
                        event.getContentIfNotHandled()?.let { profileViewState ->
                            profileViewState.profile?.let { profile ->
                                Log.d(TAG, "getProfileInfo dataState: ${profile}")
                                viewModel.setProfileData(profile)
                            }
                        }
                    }
                }
            }

        })

        viewModel.viewState.observe(viewLifecycleOwner, Observer { profileViewState ->
            profileViewState.profile?.let { profile ->
                Log.d(TAG, "getProfileInfo viewState: ${profile}")
                setProfileData(profile)
            }
        })
    }

    private fun setProfileData(profile: Profile) {
        profile_user_title.setText(profile.first_name)
        profile_text_name.setText(profile.first_name)
        profile_text_phone.setText(profile.username)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        if (showMenu)
        {
            inflater.inflate(R.menu.main_bottom_sheet, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId)
        {
            R.id.menu ->{
                findNavController().navigate(R.id.action_profileFragment_to_profileBottomSheetDialogFragment)
            }

            R.id.log_out ->{
                sessionManager.logOut()
            }

        }
        return super.onOptionsItemSelected(item)

    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            onDataStateChangeListener = context as OnDataStateChangeListener
        } catch (e: Exception) {
            Log.d(TAG, "onAttach: ${context} must implement OnDataStateChangeListener")
        }
    }

}