package com.example.bozorbek_vol2.ui.main.profile.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.main.profile.viewmodel.ProfileViewModel
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseProfileFragment : DaggerFragment() {

    val TAG = Constants.LOG
    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel:ProfileViewModel

    @Inject
    lateinit var requestManager: RequestManager



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(ProfileViewModel::class.java)
        }?:throw Exception("Invalid value")

        setAppBarConfiguration(R.id.profileFragment, activity as DaggerAppCompatActivity)

        cancelActiveJob()
    }

    fun cancelActiveJob()
    {
        viewModel.cancelActiveJob()
    }

    private fun setAppBarConfiguration(fragment: Int, activity: DaggerAppCompatActivity) {
        val appBarConfiguration = AppBarConfiguration(setOf(fragment))
        NavigationUI.setupActionBarWithNavController(
            activity, findNavController(), appBarConfiguration
        )
    }

}
