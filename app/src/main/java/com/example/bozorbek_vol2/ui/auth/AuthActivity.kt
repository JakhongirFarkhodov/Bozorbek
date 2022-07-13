package com.example.bozorbek_vol2.ui.auth

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileReadyPackageAutoOrder
import com.example.bozorbek_vol2.ui.BaseActivity
import com.example.bozorbek_vol2.ui.main.MainActivity
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.CategoryData
import kotlinx.android.synthetic.main.activity_auth.*

class AuthActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        changeStatusBarColor()

        checkAuthUser()
    }

    private fun checkAuthUser() {
        sessionManager.cachedAuthToken.observe(this, Observer { authToken ->
            Log.d(TAG, "AuthActivity authToken: ${authToken}")
            if (authToken != null && authToken.access_token != null && authToken.refresh_token != null)
            {
                navToMainActivity()
            }
        })
    }

    private fun navToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    protected fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)

            window.statusBarColor = ContextCompat.getColor(this, R.color.fruit_light_green)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(this, R.color.fruit_light_green)

        }
    }

    override fun showProgressBar(isShowLoading: Boolean) {
        if (isShowLoading)
        {
            auth_progress_bar.visibility = View.VISIBLE
        }
        else{
            auth_progress_bar.visibility = View.GONE
        }
    }

    override fun expendAppBar() {
//        TODO("Not yet implemented")
    }

    override fun getOnOrderItemCount(itemCount: Int) {
//        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun setCatalogListOfObject(list: List<Catalog>) {
        TODO("Not yet implemented")
    }

    override fun getCatalogListOfObject(): List<Catalog> {
        return ArrayList<Catalog>()
    }

    override fun setCatalogProductPosition(position: Int) {
        TODO("Not yet implemented")
    }

    override fun getCatalogProductPosition(): Int {
        return 0
    }

    override fun setListOfObjects(list: List<BasketOrderProduct>) {
        TODO("Not yet implemented")
    }

    override fun getListOfObjects(): List<BasketOrderProduct> {
        return ArrayList<BasketOrderProduct>()
    }

    override fun setReadyPackageListOfItems(list: List<ProfileReadyPackageId>) {
        TODO("Not yet implemented")
    }

    override fun getReadyPackageListOfItems(): List<ProfileReadyPackageId> {
        return ArrayList<ProfileReadyPackageId>()
    }

    override fun setCategoryReadyPackage(list: List<CategoryData>) {
        TODO("Not yet implemented")
    }

    override fun getCategoryReadyPackage(): List<CategoryData> {
        return ArrayList<CategoryData>()
    }

    override fun setCategoryReadyPackageId(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getCategoryReadyPackageId(): Int {
       return 0
    }

    override fun setProfileReadyPackageAutoOrderParameters(profileReadyPackageAutoOrder: ProfileReadyPackageAutoOrder) {
        TODO("Not yet implemented")
    }

    override fun getProfileReadyPackageAutoOrderParameters(): ProfileReadyPackageAutoOrder {
       return ProfileReadyPackageAutoOrder(0,0,0)
    }

    override fun setTriggerProfileReadyPackageAutoOrderParameters(trigger: Boolean) {
        TODO("Not yet implemented")
    }

    override fun getTriggerProfileReadyPackageAutoOrderParameters(): Boolean {
        return false
    }

    override fun setSaveButtonClick(click: Boolean) {
        TODO("Not yet implemented")
    }

    override fun isSaveButtonClick(): Boolean {
        return false
    }
}