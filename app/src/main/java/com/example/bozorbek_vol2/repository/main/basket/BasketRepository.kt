package com.example.bozorbek_vol2.repository.main.basket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.basket.request.AddAddressOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.request.ApproveOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.response.AddAddressOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.ApproveOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.BasketOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.GetBasketListAddressResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ProfileResponse
import com.example.bozorbek_vol2.persistance.main.basket.BasketDao
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.basket.state.BasketGetAddressOrderList
import com.example.bozorbek_vol2.ui.main.basket.state.BasketOrderProductList
import com.example.bozorbek_vol2.ui.main.basket.state.BasketViewState
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import com.example.bozorbek_vol2.util.ApiSuccessResponse
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BasketRepository
@Inject
constructor(
    val sessionManager: SessionManager,
    val apiServices: MainApiServices,
    val basketDao: BasketDao,
    val profileDao: ProfileDao
) {

    private var repositoryJob: Job? = null

    fun getProfileInfo(authToken: AuthToken): LiveData<DataState<BasketViewState>> {
        return object : NetworkBoundResource<ProfileResponse, Profile, BasketViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { basketViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(DataState.data(data = basketViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return profileDao.getProfileData()?.switchMap { profile ->
                    basketDao.getListOfBasketOrderProduct()?.switchMap { list ->
                        object : LiveData<BasketViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = BasketViewState(
                                    basketOrderProductList = BasketOrderProductList(list),
                                    profile = profile
                                )
                            }
                        }
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Profile?) {
                cacheObject?.let { profile ->
                    withContext(IO)
                    {
                        try {
                            launch {
                                Log.d(TAG, "updateCache: Inserting basket profile info: ${profile}")
                                profileDao.insertProfileData(profile)
                            }.join()
                        } catch (e: Exception) {
                            Log.d(
                                TAG,
                                "updateCache: Error inserting basket profile info:${profile}"
                            )
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileResponse>) {
                val profile = Profile(
                    first_name = response.body.firstName,
                    last_name = response.body.lastName,
                    username = response.body.username,
                    customer_phone = 0,
                    get_image = ""
                )
                updateCache(profile)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileResponse>> {
                return apiServices.getProfileInfo(token = "Bearer ${authToken.access_token}")
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getBasketProductOrderList(authToken: AuthToken): LiveData<DataState<BasketViewState>> {
        return object :
            NetworkBoundResource<BasketOrderResponse, List<BasketOrderProduct>, BasketViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = true,
                cancelJobIfNoInternet = true
            ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { basketViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(
                            dataState = DataState.data(
                                data = basketViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return basketDao.getListOfBasketOrderProduct()?.switchMap { list ->

                    object : LiveData<BasketViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value =
                                BasketViewState(basketOrderProductList = BasketOrderProductList(list))
                        }
                    }
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<BasketOrderProduct>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        basketDao.deleteAllListOfBasketOrderProduct()
                        for (basketOrderProduct in list) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data: ${basketOrderProduct}")
                                    basketDao.insertBasketOrderProduct(basketOrderProduct)
                                }
                            } catch (e: Exception) {
                                Log.d(
                                    TAG,
                                    "updateCache: Error inserting data: ${basketOrderProduct}"
                                )
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<BasketOrderResponse>) {
                val basketOrderList = ArrayList<BasketOrderProduct>()
                for (item in response.body.items) {
                    basketOrderList.add(
                        BasketOrderProduct(
                            id = item.product_item.id,
                            name = item.product_item.name,
                            product_name = item.product_item.product_name,
                            from = item.product_item.form,
                            color = item.product_item.color,
                            aroma = item.product_item.aroma,
                            taste = item.product_item.taste,
                            organic = item.product_item.organic,
                            origin = item.product_item.origin,
                            piece_size = item.product_item.piece_size,
                            in_piece = item.product_item.in_piece,
                            price_in_piece = item.product_item.price_in_piece,
                            discount_in_piece = item.product_item.discount_in_piece,
                            in_gramme = item.product_item.in_gramme,
                            price_in_gramme = item.product_item.price_in_gramme,
                            sum_price_gramme = item.price.toFloat(),
                            discount_in_gramme = item.product_item.discount_in_gramme,
                            size_gramme = item.product_item.size_gramme,
                            sum_of_size = item.quantity.toFloat(),
                            size_diameter = item.product_item.size_diameter,
                            expiration = item.product_item.expiration,
                            certification = item.product_item.certification,
                            condition = item.product_item.condition,
                            storage_temp = item.product_item.storage_temp,
                            description = item.product_item.description,
                            main_image = Constants.BASE_URL + item.product_item.main_image

                        )
                    )
                }

                updateCache(basketOrderList)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<BasketOrderResponse>> {
                return apiServices.getBasketOrderList(accessToken = "Bearer ${authToken.access_token}")
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

}