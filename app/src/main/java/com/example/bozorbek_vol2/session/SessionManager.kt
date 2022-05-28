package com.example.bozorbek_vol2.session

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.util.PreferenceKeys
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.ClassCastException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SessionManager
@Inject
constructor(val authTokenDao: AuthTokenDao, val application: Application, val sharedPrefEditor: SharedPreferences.Editor)
{
    val TAG = Constants.LOG
    protected val _cachedAuthToken = MutableLiveData<AuthToken?>()

    val cachedAuthToken: LiveData<AuthToken?>
        get() = _cachedAuthToken

    fun logOut()
    {
        var error_message:String? = null

        GlobalScope.launch(Dispatchers.IO) {
            try {
                Log.d(TAG, "logOut: ${_cachedAuthToken.value}")
                _cachedAuthToken.value!!.account_phone_number.let {
                    Log.d(TAG, "phone: ${it}")
                    sharedPrefEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, null).apply()
                    authTokenDao.nullAccessToken(it)
                }
            }
            catch (e: ClassCastException)
            {
                Log.d(TAG, "logOut1: ${e.message}")
                error_message = e.message
            }
            catch (e: Exception) {

                Log.d(TAG, "logOut2: ${e.message}")
                error_message = error_message + "\n" + e.message

            } finally {
                Log.d(TAG, "finally logOut: ${error_message}")
                setValue(null)
            }
        }
    }

    fun login(authToken: AuthToken)
    {
        setValue(authToken)
    }

    private fun setValue(authToken: AuthToken?) {
        GlobalScope.launch(Dispatchers.Main) {
            if (_cachedAuthToken.value != authToken)
            {
                _cachedAuthToken.value = authToken
            }
        }
    }

    fun isInternetAvailable():Boolean{
        var result:Boolean = false
        val connectivityManager = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when{
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        }
        else{
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when(type)
                    {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}