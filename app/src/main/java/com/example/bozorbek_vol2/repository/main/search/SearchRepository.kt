package com.example.bozorbek_vol2.repository.main.search

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.Data
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.search.state.CheckPreviousAuthUser
import com.example.bozorbek_vol2.ui.main.search.state.SearchViewState
import com.example.bozorbek_vol2.util.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    val accountPropertiesDao: AccountPropertiesDao,
    val authTokenDao: AuthTokenDao,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences
)
{
    private val TAG = Constants.LOG
    private var repositoryJob:Job? = null

    fun checkPreviousAuthUser():LiveData<DataState<SearchViewState>>
    {
        val phone = sharedPreferences.getString(PreferenceKeys.PREVIOUS_AUTH_USER, null)
        if (phone.isNullOrBlank())
        {
            Log.d(TAG, "checkPreviousAuthUser: Пользыватель к сожелению не найден...")
            return phoneNotFound()
        }

        return object : NetworkBoundResource<Void, Void, SearchViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = false
        )
        {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { searchViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = searchViewState, response = Response(message = "Пользыватель имеет токен", responseType = ResponseType.Dialog())))
                    })
                }
            }

            override fun loadFromCache(): LiveData<SearchViewState> {
                Log.d(TAG, "search: loadFromCache: ${phone}")
                return accountPropertiesDao.searchByPhoneNumber(phone)?.switchMap { accountProperties ->
                    authTokenDao.searchByPhoneNumber(accountProperties.phone_number)?.switchMap { authToken ->
                        object : LiveData<SearchViewState>()
                        {
                            override fun onActive() {
                                super.onActive()
                                authToken.access_token?.let { access_token ->
                                    authToken.refresh_token?.let { refresh_token ->
                                        value = SearchViewState(checkPreviousAuthUser = CheckPreviousAuthUser(access_token = access_token, refresh_token = refresh_token, phone_number = phone))
                                    }
                                }
                            }
                        }
                    }?:AbsentLiveData.create()
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<Void>) {
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun phoneNotFound(): LiveData<DataState<SearchViewState>> {
        return object : LiveData<DataState<SearchViewState>>()
        {
            override fun onActive() {
                super.onActive()
                value = DataState.data(data = null, response = Response(message = "Вы еще не зарегистрированы.", responseType = ResponseType.Toast()))
            }
        }
    }
}