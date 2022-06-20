package com.example.bozorbek_vol2.ui.main.home.fragment

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.util.Constants
import dagger.android.support.DaggerAppCompatActivity
import dagger.android.support.DaggerFragment

abstract class BaseHomeFragment : DaggerFragment() {

    val TAG = Constants.LOG

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setAppBarConfiguration(R.id.homeFragment, activity as DaggerAppCompatActivity)
    }

    private fun setAppBarConfiguration(fragment: Int, activity: DaggerAppCompatActivity) {
        val appBarConfiguration = AppBarConfiguration(setOf(fragment))
        NavigationUI.setupActionBarWithNavController(
            activity, findNavController(), appBarConfiguration
        )
    }


}