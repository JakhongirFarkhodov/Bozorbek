package com.example.bozorbek_vol2.util

import android.app.Activity
import android.content.Context
import androidx.annotation.IdRes
import androidx.annotation.NavigationRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.bozorbek_vol2.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavController
constructor(
    val context: Context,
    @IdRes val containerId: Int,
    @IdRes val appDestinationChange: Int,
    val navGraphProvider: NavGraphProvider,
    val onNavigationGraphChangeListener: OnNavigationGraphChangeListener?
) {
    val TAG = Constants.LOG
    lateinit var activity: Activity
    lateinit var fragmentManager: FragmentManager
    lateinit var onNavigationItemChangeListener: OnNavigationItemChangeListener
    private var navigationBackStack = BackStack.of(appDestinationChange)

    init {
        if (context is Activity) {
            activity = context
            fragmentManager = (activity as FragmentActivity).supportFragmentManager
        }
    }

    fun onNavigationItemSelected(itemId: Int = navigationBackStack.last()): Boolean {
        val fragments = fragmentManager.findFragmentByTag(itemId.toString())
            ?: NavHostFragment.create(navGraphProvider.getItemId(itemId))

        fragmentManager
            .beginTransaction()
            .setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out,
                R.anim.fade_in,
                R.anim.fade_out
            )
            .addToBackStack(null)
            .replace(containerId, fragments, itemId.toString())
            .commit()

        navigationBackStack.moveLast(itemId)
        onNavigationItemChangeListener.onNavigationItemChange(itemId)
        onNavigationGraphChangeListener?.onNavigationGraphChange()

        return true
    }

    fun backPressed() {
        val navController = fragmentManager.findFragmentById(containerId)!!.findNavController()
        when {
            navController.backQueue.size > 2 -> {
                navController.popBackStack()
            }

            navigationBackStack.size > 1 -> {
                navigationBackStack.removeLast()
                onNavigationItemSelected()
            }

            navigationBackStack.last() != appDestinationChange ->{
                navigationBackStack.removeLast()
                navigationBackStack.add(0,appDestinationChange)
                onNavigationItemSelected()
            }
            else -> {
                activity.finish()
            }

        }
    }

    class BackStack : ArrayList<Int>() {
        companion object {
            fun of(vararg elements: Int): BackStack {
                val b = BackStack()
                b.addAll(elements.toTypedArray())
                return b
            }
        }

        fun removeLast() = removeAt(size - 1)
        fun moveLast(itemId: Int) {
            remove(itemId)
            add(itemId)
        }
    }

    interface OnNavigationItemChangeListener {
        fun onNavigationItemChange(item: Int)
    }

    fun setOnNavigationItemChange(listener: (itemId: Int) -> Unit) {
        this.onNavigationItemChangeListener = object : OnNavigationItemChangeListener {
            override fun onNavigationItemChange(item: Int) {
                listener.invoke(item)
            }
        }
    }

    interface NavGraphProvider {
        @NavigationRes
        fun getItemId(itemId: Int): Int
    }

    interface OnNavigationGraphChangeListener {
        fun onNavigationGraphChange()
    }

    interface OnNavigationItemReselectedListener {
        fun onNavigationItemReselected(navController: NavController, fragment: Fragment)
    }

}

fun BottomNavigationView.setUpWithNavigation(
    bottomNavController: BottomNavController,
    onNavigationItemReselectedListener: BottomNavController.OnNavigationItemReselectedListener
) {
    setOnItemSelectedListener {
        bottomNavController.onNavigationItemSelected(it.itemId)
    }

    setOnItemReselectedListener {
        bottomNavController
            .fragmentManager
            .findFragmentById(bottomNavController.containerId)!!
            .childFragmentManager
            .fragments[0]?.let { fragment ->
            onNavigationItemReselectedListener.onNavigationItemReselected(
                bottomNavController.activity.findNavController(bottomNavController.containerId),
                fragment
            )
        }
    }

    bottomNavController.setOnNavigationItemChange {
        menu.findItem(it).isChecked = true
    }
}