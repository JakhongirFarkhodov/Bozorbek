package com.example.bozorbek_vol2.repository.main.search

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.main.search.SearchProduct
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.search.response.SearchProductResponse
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.persistance.main.search.SearchDao
import com.example.bozorbek_vol2.repository.JobManager
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.search.state.CheckPreviousAuthUser
import com.example.bozorbek_vol2.ui.main.search.state.SearchViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import com.example.bozorbek_vol2.util.ApiSuccessResponse
import com.example.bozorbek_vol2.util.GenericApiResponse
import com.example.bozorbek_vol2.util.PreferenceKeys
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository
@Inject
constructor(
    val accountPropertiesDao: AccountPropertiesDao,
    val authTokenDao: AuthTokenDao,
    val sessionManager: SessionManager,
    val sharedPreferences: SharedPreferences,
    val apiServices: MainApiServices,
    val searchDao: SearchDao
) : JobManager("SearchRepository")
{


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
                addJob("checkPreviousAuthUser", job)
            }

        }.asLiveData()
    }

    fun searchProduct(query:String): LiveData<DataState<SearchViewState>>
    {
        return object : NetworkBoundResource<SearchProductResponse, List<SearchProduct>, SearchViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { searchViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = searchViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<SearchViewState> {
                return searchDao.getAllSearchProduct()?.switchMap { list ->
                    object : LiveData<SearchViewState>()
                    {
                        override fun onActive() {
                            super.onActive()
                            value = SearchViewState(searchProductList = list)
                        }
                    }
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<SearchProduct>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        searchDao.deleteAllSearchProduct()
                        for (item in list)
                        {
                            try {
                                Log.d(TAG, "updateCache: Inserting data:${item}")
                                launch {
                                    searchDao.insertSearchProduct(item)
                                }.join()
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache: Error inserting data:${item}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<SearchProductResponse>) {
                val product_list:ArrayList<SearchProduct> = ArrayList()
                for (item in response.body.results)
                {
                    product_list.add(
                        SearchProduct(
                        name = item.name,
                        slug = item.slug,
                        absolute_url = item.absolute_url
                    )
                    )
                }

                updateCache(product_list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<SearchProductResponse>> {
                return apiServices.searchProduct(query = query)
            }

            override fun setJob(job: Job) {
                addJob("searchProduct", job)
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