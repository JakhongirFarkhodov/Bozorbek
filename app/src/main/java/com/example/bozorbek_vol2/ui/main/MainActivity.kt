package com.example.bozorbek_vol2.ui.main

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.BaseActivity
import com.example.bozorbek_vol2.ui.OnDataStateChangeListener
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.catalog.fragment.CatalogProductFragment
import com.example.bozorbek_vol2.ui.main.catalog.fragment.CatalogViewProductFragment
import com.example.bozorbek_vol2.ui.main.home.fragment.HomeProductFragment
import com.example.bozorbek_vol2.ui.main.home.fragment.HomeViewProductFragment
import com.example.bozorbek_vol2.util.BottomNavController
import com.example.bozorbek_vol2.util.BottomNavController.*
import com.example.bozorbek_vol2.util.setUpWithNavigation
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity(), NavGraphProvider, OnNavigationGraphChangeListener, OnNavigationItemReselectedListener {

    private var count:Int = 0
    private lateinit var bottomNavigationView: BottomNavigationView
    private val bottomNavController: BottomNavController by lazy(LazyThreadSafetyMode.NONE)
    {
        BottomNavController(
            this,
            R.id.main_nav_host_fragment,
            R.id.nav_search,
            this,
            this
        )
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        changeBottomNavigationView(savedInstanceState)
        changeAppBar()
        changeStatusBarColor()
        checkAuthUser()

    }

    private fun checkAuthUser() {
        sessionManager.cachedAuthToken.observe(this, Observer { authToken ->
            Log.d(TAG, "MainActivity authToken: ${authToken}")
            if (authToken == null || authToken.access_token == null || authToken.refresh_token == null)
            {
                navToAuthActivity()
            }
        })
    }

    private fun navToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivity(intent)
        finish()
    }


    private fun changeBottomNavigationView(savedInstanceState: Bundle?) {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setUpWithNavigation(bottomNavController, this)

        if (savedInstanceState == null) {
            bottomNavController.onNavigationItemSelected()
        }

        search_products.setOnClickListener {
            bottomNavController.onNavigationItemSelected(R.id.nav_search)
        }

        bottomNavigationView.background = null
    }

    private fun changeAppBar() {
        setSupportActionBar(tool_bar)
    }

    protected fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)

            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(this, R.color.status_bar_color)
        }
    }

    override fun getItemId(itemId: Int): Int {
        return when (itemId) {
            R.id.nav_home -> {
                R.navigation.main_home_nav_graph
            }

            R.id.nav_catalog -> {
                R.navigation.main_catalog_nav_graph
            }

            R.id.nav_search -> {
                R.navigation.main_search_nav_graph
            }

            R.id.nav_basket -> {
                R.navigation.main_basket_nav_graph
            }

            R.id.nav_profile -> {
                R.navigation.main_profile_nav_graph
            }

            else -> {
                R.navigation.main_home_nav_graph
            }
        }
    }

    override fun onNavigationGraphChange() {
        expendAppBar()
    }

    override fun onNavigationItemReselected(navController: NavController, fragment: Fragment) {
        when (fragment) {
            is HomeProductFragment -> {
                navController.navigate(R.id.action_homeProductFragment_to_homeFragment)
            }
            is HomeViewProductFragment -> {
                navController.navigate(R.id.action_homeViewProductFragment_to_homeFragment)
            }
            is CatalogProductFragment -> {
                navController.navigate(R.id.action_catalogProductFragment_to_catalogFragment)
            }
            is CatalogViewProductFragment -> {
                navController.navigate(R.id.action_catalogViewProductFragment_to_catalogFragment)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        bottomNavController.backPressed()
    }


    override fun showProgressBar(isShowLoading: Boolean) {
        if (isShowLoading)
        {
            main_progress_bar.visibility = View.VISIBLE
        }
        else{
            main_progress_bar.visibility = View.GONE
        }
    }

    override fun expendAppBar() {
        findViewById<AppBarLayout>(R.id.app_bar).setExpanded(true)
    }

    override fun getOnOrderItemCount(itemCount: Int) {
        Log.d(TAG, "getOnOrderItemCount: ${itemCount}")
        count = itemCount
        if (count != 0) {
            bottomNavigationView.getOrCreateBadge(R.id.nav_basket).apply {
                Log.d(TAG, "changeBottomNavigationView: ${count}")
                number = count
                isVisible = true
            }
        }
        else
        {
            bottomNavigationView.getBadge(R.id.nav_basket)?.apply {
                number = 0
            }
            bottomNavigationView.removeBadge(R.id.nav_basket)
        }

    }

    override fun getItemCount(): Int {
        return count
    }
}