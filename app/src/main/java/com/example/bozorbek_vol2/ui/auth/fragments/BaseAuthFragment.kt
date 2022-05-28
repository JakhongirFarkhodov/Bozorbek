package com.example.bozorbek_vol2.ui.auth.fragments

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.auth.viewmodel.AuthViewModel
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseAuthFragment : DaggerFragment() {

    val TAG = Constants.LOG

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var providerFactory: ViewModelProviderFactory
    lateinit var viewModel: AuthViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(AuthViewModel::class.java)
        }?:throw Exception("Invalid value")
    }

}