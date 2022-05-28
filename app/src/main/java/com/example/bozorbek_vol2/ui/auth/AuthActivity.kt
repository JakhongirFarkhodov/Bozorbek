package com.example.bozorbek_vol2.ui.auth

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.ui.BaseActivity
import com.example.bozorbek_vol2.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_auth.*
import javax.inject.Inject

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
}