package com.example.bozorbek_vol2.ui

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import androidx.core.content.ContextCompat
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.app.BaseApplication
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.main.MainActivity
import com.example.bozorbek_vol2.util.Constants
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject

abstract class BaseActivity : DaggerAppCompatActivity(), OnDataStateChangeListener {

    val TAG = Constants.LOG

    @Inject
    lateinit var sessionManager: SessionManager

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
        when(response.responseType)
        {
            is ResponseType.Toast ->{
                response.message?.let { message ->
                    showToast(message)
                }
            }
            is ResponseType.Dialog ->{
                response.message?.let { message ->
                    showErrorDialog(message = message)
                }
            }
            is ResponseType.None ->{
                Log.d(TAG, "handleErrorResponse: ")
            }
        }
    }

    private fun handleSuccessResponse(response: Response) {
        when(response.responseType)
        {
            is ResponseType.Toast ->{
                response.message?.let { message ->
                    showToast(message = message)
                }
            }
            is ResponseType.Dialog ->{
                response.message?.let { message ->
                    showSuccessDialog(message = message)
                }
            }
            is ResponseType.None ->{
                Log.d(TAG, "handleSuccessResponse: ")
            }
        }
    }

    abstract fun showProgressBar(isShowLoading:Boolean)



}