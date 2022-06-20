package com.example.bozorbek_vol2.ui.main.search.fragment

import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.main.search.viewmodel.SearchViewModel
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.viewmodels.ViewModelProviderFactory
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment
import javax.inject.Inject

abstract class BaseSearchFragment : DaggerFragment() {

    val TAG = Constants.LOG

    @Inject
    protected lateinit var providerFactory: ViewModelProviderFactory
    protected lateinit var viewModel:SearchViewModel
    @Inject
    protected lateinit var requestManager: RequestManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = activity?.run {
            ViewModelProvider(this, providerFactory).get(SearchViewModel::class.java)
        }?:throw Exception("Invalid value")

        setAppBarConfiguration(R.id.searchFragment, activity as DaggerAppCompatActivity)

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