package com.example.bozorbek_vol2.repository.main.basket

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.basket.request.AddAddressOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.request.ApproveOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.response.AddAddressOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.ApproveOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.BasketOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.GetBasketListAddressResponse
import com.example.bozorbek_vol2.persistance.main.basket.BasketDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
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
    val basketDao: BasketDao
) {

    private var repositoryJob: Job? = null

    fun getListOfBasketProductItemOrder(authToken: AuthToken):LiveData<DataState<BasketViewState>>
    {
        return object : NetworkBoundResource<BasketOrderResponse, List<BasketOrderProduct>, BasketViewState>(
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
                    result.addSource(loadCache, Observer { basketViewState ->
                        onCompleteJob(dataState = DataState.data(data = basketViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<BasketViewState> {
                return basketDao.getListOfBasketOrderProduct()?.switchMap { list ->
                    object : LiveData<BasketViewState>()
                    {
                        override fun onActive() {
                            super.onActive()
                            value = BasketViewState(basketOrderProductList = BasketOrderProductList(list))
                        }
                    }
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<BasketOrderProduct>?) {
                cacheObject?.let { list ->  
                    withContext(IO)
                    {
                        for (basketProductOrder in list)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data:${basketProductOrder}")
                                    basketDao.insertBasketOrderProduct(basketProductOrder)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error inserting data:${basketProductOrder}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<BasketOrderResponse>) {
                val list = ArrayList<BasketOrderProduct>()
                for (basketProductOrder in response.body.items)
                {
                    list.add(BasketOrderProduct(
                        id = basketProductOrder.product_item.id,
                        name = basketProductOrder.product_item.name,
                        product_name = basketProductOrder.product_item.product_name,
                        from = basketProductOrder.product_item.from,
                        color = basketProductOrder.product_item.color,
                        aroma = basketProductOrder.product_item.aroma,
                        taste = basketProductOrder.product_item.taste,
                        organic = basketProductOrder.product_item.organic,
                        origin = basketProductOrder.product_item.origin,
                        piece_size = basketProductOrder.product_item.piece_size,
                        in_piece = basketProductOrder.product_item.in_piece,
                        price_in_piece = basketProductOrder.product_item.price_in_piece,
                        discount_in_piece = basketProductOrder.product_item.discount_in_piece,
                        in_gramme = basketProductOrder.product_item.in_gramme,
                        price_in_gramme = basketProductOrder.product_item.price_in_gramme,
                        sum_price_gramme = basketProductOrder.price.toFloat(),
                        discount_in_gramme = basketProductOrder.product_item.discount_in_gramme,
                        size_gramme = basketProductOrder.product_item.size_gramme,
                        sum_of_size = basketProductOrder.quantity.toFloat(),
                        size_diameter = basketProductOrder.product_item.size_diameter,
                        expiration = basketProductOrder.product_item.expiration,
                        certification = basketProductOrder.product_item.certification,
                        condition = basketProductOrder.product_item.condition,
                        storage_temp = basketProductOrder.product_item.storage_temp,
                        description = basketProductOrder.product_item.description,
                        main_image = Constants.BASE_URL + basketProductOrder.product_item.main_image
                    ))
                }

                updateCache(list)
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

    fun addAddressProductOrder(authToken: AuthToken, full_address:String, latitude:String, longtitude:String):LiveData<DataState<BasketViewState>>
    {
        return object : NetworkBoundResource<AddAddressOrderResponse, Any, BasketViewState>(
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
                TODO("Not yet implemented")
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<AddAddressOrderResponse>) {
                withContext(IO)
                {
                    onCompleteJob(dataState = DataState.data(data = BasketViewState(basketGetAddedAddressMessage = response.body.message), response = Response(message = response.body.message, responseType = ResponseType.Toast())))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<AddAddressOrderResponse>> {
                return apiServices.setOrderAddress("Bearer ${authToken.access_token}", addAddressOrderRequest = AddAddressOrderRequest(full_address, latitude, longtitude))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getOrderAddressList(authToken: AuthToken):LiveData<DataState<BasketViewState>>
    {
        return object : NetworkBoundResource<List<GetBasketListAddressResponse>, Any, BasketViewState>(
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
                TODO("Not yet implemented")
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<List<GetBasketListAddressResponse>>) {
                withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = BasketViewState(basketGetAddressOrderList = BasketGetAddressOrderList(response.body)), response = null))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<List<GetBasketListAddressResponse>>> {
                return apiServices.getBasketAddressList(accessToken = "Bearer ${authToken.access_token}")
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun approveOrder(authToken: AuthToken, address_id:String):LiveData<DataState<BasketViewState>>
    {
        return object : NetworkBoundResource<ApproveOrderResponse, Any, BasketViewState>(
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
                TODO("Not yet implemented")
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ApproveOrderResponse>) {
                withContext(Main)
                {
                    var message = response.body.message
                    if (message.equals("Success"))
                    {
                        message = "Заказ оформлен"
                    }
                    onCompleteJob(dataState = DataState.data(data = null, response = Response(message = message, responseType = ResponseType.Dialog())))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ApproveOrderResponse>> {
                return apiServices.approveOrder(accessToken = "Bearer ${authToken.access_token}", approveOrderRequest = ApproveOrderRequest(address_id = address_id))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }
}