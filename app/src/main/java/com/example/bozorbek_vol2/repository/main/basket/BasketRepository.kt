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
import com.example.bozorbek_vol2.network.main.network_services.basket.request.BasketRemoveProductRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.response.*
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ProfileResponse
import com.example.bozorbek_vol2.persistance.main.basket.BasketDao
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.Data
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.basket.state.BasketGetAddressOrderList
import com.example.bozorbek_vol2.ui.main.basket.state.BasketOrderProductList
import com.example.bozorbek_vol2.ui.main.basket.state.BasketViewState
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
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    onCompleteJob(DataState.data(data = null, response = null))
                }
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return AbsentLiveData.create()
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
                    profileDao.getProfileData()?.switchMap { profile ->
                        object : LiveData<BasketViewState>() {
                            override fun onActive() {
                                super.onActive()
                                Log.d(TAG, "loadFromCache: ${list}")
                                value = BasketViewState(
                                    basketOrderProductList = BasketOrderProductList(list),
                                    profile = profile
                                )
                            }
                        }
                    } ?: AbsentLiveData.create()
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
                var count = 0
                for (item in response.body.items) {
                    count++
                    basketOrderList.add(
                        BasketOrderProduct(
                            basket_id = count,
                            id = item.product_item.id,
                            basket_product_item_id = item.id,
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

    fun removeBasketProductById(
        authToken: AuthToken,
        item_id: String
    ): LiveData<DataState<BasketViewState>> {
        return object : NetworkBoundResource<BasketRemoveProductResponse, Void, BasketViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<BasketRemoveProductResponse>) {
                withContext(Main)
                {
                    if (response.body.remove_message.lowercase().equals("success")) {
                        onCompleteJob(
                            dataState = DataState.data(
                                data = null,
                                response = Response(
                                    message = "Remove successfully",
                                    responseType = ResponseType.Toast()
                                )
                            )
                        )
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<BasketRemoveProductResponse>> {
                Log.d(TAG, "show remove id: ${item_id}")
                return apiServices.removeBasketOrderProductById(
                    accessToken = "Bearer ${authToken.access_token}",
                    basketRemoveProductRequest = BasketRemoveProductRequest(remove_item_id = item_id)
                )
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun setOrderAddress(
        authToken: AuthToken,
        full_address: String,
        latitude: String,
        longtitude: String
    ): LiveData<DataState<BasketViewState>> {
        return object : NetworkBoundResource<AddAddressOrderResponse, Void, BasketViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<AddAddressOrderResponse>) {
                withContext(Main)
                {
                    if (response.body.message.lowercase().equals("success"))
                    {
                        onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "Address added successfully", responseType = ResponseType.Toast())))
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<AddAddressOrderResponse>> {
                return apiServices.setOrderAddress(
                    accessToken = "Bearer ${authToken.access_token}",
                    AddAddressOrderRequest(
                        full_address, latitude, longtitude
                    )
                )
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getAddressList(authToken: AuthToken):LiveData<DataState<BasketViewState>>
    {
        return object : NetworkBoundResource<List<GetBasketListAddressResponse>, Void, BasketViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<List<GetBasketListAddressResponse>>) {
                withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = BasketViewState(basketGetAddressOrderList = BasketGetAddressOrderList(list = response.body)), response = null))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<List<GetBasketListAddressResponse>>> {
                return apiServices.getBasketAddressList("Bearer ${authToken.access_token}")
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun approveBasketOrder(authToken: AuthToken, address_id:String, name:String, phone_num:String):LiveData<DataState<BasketViewState>>
    {
        return object : NetworkBoundResource<ApproveOrderResponse, Void, BasketViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ApproveOrderResponse>) {
                withContext(Main)
                {
                    if (response.body.message.lowercase().equals("success"))
                    {
                        onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "Заказ успешно оформлен", responseType = ResponseType.Toast())))
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ApproveOrderResponse>> {
                return apiServices.approveOrder(accessToken = "Bearer ${authToken.access_token}", approveOrderRequest = ApproveOrderRequest(address_id, name, phone_num))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

}