package com.example.bozorbek_vol2.ui

import android.Manifest
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.util.Constants
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), OnDataStateChangeListener {

    val TAG = Constants.LOG

    @Inject
    lateinit var sessionManager: SessionManager

    @Inject
    lateinit var editor: SharedPreferences.Editor

    override fun onDataStateChange(dataState: DataState<*>) {
        showProgressBar(isShowLoading = dataState.loading.isLoading)
        dataState.data?.let { data ->
            data.response?.let { event ->
                event.getContentIfNotHandled()?.let { response ->
                    handleSuccessResponse(response)
                }
            }
        }
        dataState.stateError?.let { event ->
            event.getContentIfNotHandled()?.let { stateError ->
                stateError.response?.let { response ->
                    handleErrorResponse(response)
                }
            }
        }
    }

    private fun handleErrorResponse(response: Response) {
        when (response.responseType) {
            is ResponseType.Toast -> {
                response.message?.let { message ->
                    showToast(message)
                }
            }
            is ResponseType.Dialog -> {
                response.message?.let { message ->
                    showErrorDialog(message = message)
                }
            }
            is ResponseType.None -> {
                Log.d(TAG, "handleErrorResponse: ")
            }
        }
    }

    private fun handleSuccessResponse(response: Response) {
        when (response.responseType) {
            is ResponseType.Toast -> {
                response.message?.let { message ->
                    showToast(message = message)
                }
            }
            is ResponseType.Dialog -> {
                response.message?.let { message ->
                    showSuccessDialog(message = message)
                }
            }
            is ResponseType.None -> {
                Log.d(TAG, "handleSuccessResponse: ")
            }
        }
    }

    abstract fun showProgressBar(isShowLoading: Boolean)


    override fun isStoragePermissionGranted(): Boolean {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ),Constants.PERMISSION_REQUEST_READ_STORAGE
            )
            return false
        }
        else{
            return true
        }

    }


}