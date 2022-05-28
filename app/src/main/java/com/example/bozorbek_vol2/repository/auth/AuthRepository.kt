package com.example.bozorbek_vol2.repository.auth

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.auth.AccountProperties
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.network.auth.AuthApiService
import com.example.bozorbek_vol2.network.auth.network_services.request.LoginRequest
import com.example.bozorbek_vol2.network.auth.network_services.request.RegisterRequest
import com.example.bozorbek_vol2.network.auth.network_services.request.SetPasswordRequest
import com.example.bozorbek_vol2.network.auth.network_services.request.VerifySmsCodeRequest
import com.example.bozorbek_vol2.network.auth.network_services.response.LoginResponse
import com.example.bozorbek_vol2.network.auth.network_services.response.RegisterResponse
import com.example.bozorbek_vol2.network.auth.network_services.response.SetPasswordResponse
import com.example.bozorbek_vol2.network.auth.network_services.response.VerifySmsCodeResponse
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.auth.state.*
import com.example.bozorbek_vol2.util.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AuthRepository @Inject constructor(
    val apiService: AuthApiService,
    val accountPropertiesDao: AccountPropertiesDao,
    val authTokenDao: AuthTokenDao,
    val sessionManager: SessionManager,
    val sharedPreferencesEditor: SharedPreferences.Editor
) {

    private val TAG = Constants.LOG
    private var repositoryJob:Job? = null

    fun registrationResponse(first_name:String, phone_number:String):LiveData<DataState<AuthViewState>>
    {
        val registrationError = RegistrationValue(first_name = first_name, phone_number = phone_number).checkRegistrationValue()
        if (!registrationError.equals(RegistrationValue.RegistrationError.none()))
        {
            return onErrorFields(error_message = registrationError, responseType = ResponseType.Dialog())
        }

        return object : NetworkBoundResource<RegisterResponse, Any, AuthViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<RegisterResponse>) {
                withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = AuthViewState(registrationValue = RegistrationValue(phone_number = phone_number)),response = null))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<RegisterResponse>> {
                return apiService.registerRequest(registerRequest = RegisterRequest(phone_number))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun verifySmsCodeResponse(phone_number: String, sms_code:String):LiveData<DataState<AuthViewState>>
    {
        val smsCodeError = VerifySmsCodeValue(sms_code).checkVerifySmsCode()
        if (!smsCodeError.equals(VerifySmsCodeValue.VerifySmsCodeError.none()))
        {
            return onErrorFields(error_message = smsCodeError, responseType = ResponseType.Dialog())
        }

        return object : NetworkBoundResource<VerifySmsCodeResponse, Any, AuthViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<VerifySmsCodeResponse>) {
                withContext(Main)
                {
                    Log.d(TAG, "handleSuccessResponse: ${response.body}")
                    onCompleteJob(dataState = DataState.data(data = AuthViewState(verifySmsCodeValue = VerifySmsCodeValue(responseMessage = response.body.message)), response = null))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<VerifySmsCodeResponse>> {
                return apiService.verifySmsCodeRequest(verifySmsCodeRequest = VerifySmsCodeRequest(
                    phone_num = phone_number,
                    sms_code = sms_code
                ))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun setPasswordResponse(phone_number: String, sms_code: String, first_name: String, password:String, confirm_password:String):LiveData<DataState<AuthViewState>>
    {
        val passwordError = PasswordValue(password = password, confirm_password = confirm_password).checkPasswordValue()
        if (!passwordError.equals(PasswordValue.PasswordError.none()))
        {
            return onErrorFields(error_message = passwordError, responseType = ResponseType.Dialog())
        }

        return object : NetworkBoundResource<SetPasswordResponse, Any, AuthViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<SetPasswordResponse>) {
                withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = AuthViewState(passwordValue = PasswordValue(username = response.body.username)), response = null))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<SetPasswordResponse>> {
                return apiService.setPasswordRequest(setPasswordRequest = SetPasswordRequest(
                    phone_num = phone_number,
                    sms_code = sms_code,
                    password = password,
                    first_name = first_name
                ))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun loginResponse(username:String, password: String):LiveData<DataState<AuthViewState>>
    {
        val loginError = LoginValue(phone_number = username, password = password).checkLoginFields()
        if (!loginError.equals(LoginValue.LoginError.none()))
        {
            return onErrorFields(error_message = loginError, responseType = ResponseType.Dialog())
        }

        return object : NetworkBoundResource<LoginResponse, Any, AuthViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<AuthViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<LoginResponse>) {
                withContext(Main)
                {

                    accountPropertiesDao.insertOrReplace(accountProperties = AccountProperties(
                        phone_number = username,
                        username = ""
                    ))

                    val result = authTokenDao.insertAuthToken(authToken = AuthToken(
                        account_phone_number = username,
                        access_token = response.body.access_token,
                        refresh_token = response.body.refresh_token
                    ))

                    if (result < 1)
                    {
                        return@withContext onErrorReturn(error_message = "Невозможно сохранить токен", shouldUseToast = true, shouldUseDialog = false)
                    }

                    val sharedPreferencesEditor = sharedPreferencesEditor.putString(PreferenceKeys.PREVIOUS_AUTH_USER, username)
                    sharedPreferencesEditor.apply()

                    onCompleteJob(DataState.data(data = AuthViewState(loginValue = LoginValue(phone_number = username, access_token = response.body.access_token, refreshToken = response.body.refresh_token)), response = null))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<LoginResponse>> {
                return apiService.loginRequest(loginRequest = LoginRequest(phone_number = username, password = password))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun onErrorFields(error_message:String, responseType: ResponseType): LiveData<DataState<AuthViewState>> {
        return object : LiveData<DataState<AuthViewState>>()
        {
            override fun onActive() {
                super.onActive()
                value = DataState.error(response = Response(message = error_message, responseType = responseType))
            }
        }
    }

}