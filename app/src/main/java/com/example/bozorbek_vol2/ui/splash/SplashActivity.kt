package com.example.bozorbek_vol2.ui.splash

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileReadyPackageAutoOrder
import com.example.bozorbek_vol2.ui.BaseActivity
import com.example.bozorbek_vol2.ui.main.MainActivity
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.CategoryData
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : BaseActivity() {
    override fun showProgressBar(isShowLoading: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        changeStatusBarColor()


        GlobalScope.launch(Main) {
            delay(1500)
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

    }

    protected fun changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.setSystemBarsAppearance(
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS)

            window.statusBarColor = ContextCompat.getColor(this, R.color.splash_green)
        } else {
            @Suppress("DEPRECATION")
            window.decorView.systemUiVisibility = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            } else {
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            window.statusBarColor = ContextCompat.getColor(this, R.color.splash_green)
        }
    }

    override fun expendAppBar() {
        TODO("Not yet implemented")
    }

    override fun getOnOrderItemCount(itemCount: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun setListOfObjects(list: List<BasketOrderProduct>) {
        TODO("Not yet implemented")
    }

    override fun getListOfObjects(): List<BasketOrderProduct> {
        return ArrayList<BasketOrderProduct>()
    }

    override fun setReadyPackageListOfItems(list: List<ProfileReadyPackageId>) {

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