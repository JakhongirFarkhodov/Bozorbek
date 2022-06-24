package com.example.bozorbek_vol2.repository.main.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.main.home.HomeDiscountProducts
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.model.main.home.HomeSliderImage
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.home.response.HomeDiscountProductsResponse
import com.example.bozorbek_vol2.network.main.network_services.home.response.HomeRandomProductsResponse
import com.example.bozorbek_vol2.network.main.network_services.home.response.HomeSliderImagesResponse
import com.example.bozorbek_vol2.persistance.main.home.HomeDao
import com.example.bozorbek_vol2.repository.JobManager
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.home.state.HomeViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import com.example.bozorbek_vol2.util.ApiSuccessResponse
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.util.GenericApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class HomeRepository
@Inject
constructor(
    val sessionManager: SessionManager,
    val homeDao: HomeDao,
    val apiServices: MainApiServices
) : JobManager("HomeRepository") {
    fun getAllSliderImages(): LiveData<DataState<HomeViewState>> {
        Log.d(TAG, "getAllSliderImages: triggered")
        return object :
            NetworkBoundResource<HomeSliderImagesResponse, List<HomeSliderImage>, HomeViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = false,
                cancelJobIfNoInternet = false
            ) {
            override suspend fun createCacheAndReturn() {
              withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "Image downloaded", responseType = ResponseType.None())))

                }
            }

            override fun loadFromCache(): LiveData<HomeViewState> {
                return AbsentLiveData.create()
//                return homeDao.getAllSliderImages()?.switchMap { list ->
//                    object : LiveData<HomeViewState>()
//                    {
//                        override fun onActive() {
//                            super.onActive()
//                            value = HomeViewState(listOfSliderImage = list)
//                        }
//                    }
//                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<HomeSliderImage>?) {
                cacheObject?.let { list ->
                    withContext(Dispatchers.IO)
                    {
                        for (item in list)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache:Inserting data:${item}")
                                    homeDao.insertSliderImage(item)
                                }.join()
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache:Error inserting image:${item}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<HomeSliderImagesResponse>) {
                val list = ArrayList<HomeSliderImage>()

                for (item in response.body.results)
                {
                    list.add(
                        HomeSliderImage(
                        name = item.name,
                        image = Constants.BASE_URL + item.image,
                            text = item.text
                    )
                    )
                }

                updateCache(list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<HomeSliderImagesResponse>> {
                return apiServices.getSliderImages()
            }

            override fun setJob(job: Job) {
                addJob("getAllSliderImages", job)
            }

        }.asLiveData()
    }

    fun getRandomProducts():LiveData<DataState<HomeViewState>>
    {
        return object : NetworkBoundResource<List<HomeRandomProductsResponse>, List<HomeRandomProducts>, HomeViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = false
        )
        {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "rounded products downloaded", responseType = ResponseType.None())))

                }
            }

            override fun loadFromCache(): LiveData<HomeViewState> {
                return AbsentLiveData.create()
//                return homeDao.getAllRandomProducts()?.switchMap { list ->
//                    object : LiveData<HomeViewState>()
//                    {
//                        override fun onActive() {
//                            super.onActive()
//                            value = HomeViewState(listOfRandomPRoducts = list)
//                        }
//                    }
//                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<HomeRandomProducts>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        for (item in list)
                        {
                            try {
                                Log.d(TAG, "updateCache: Inserting data:${item}")
                                launch {
                                    homeDao.insertRandomProduct(item)
                                }
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache: Error inserting data:${item}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<List<HomeRandomProductsResponse>>) {
                val list = ArrayList<HomeRandomProducts>()
                for (item in response.body)
                {
                    list.add(HomeRandomProducts(
                        name = item.name,
                        get_absolute_url = item.get_absolute_url,
                        slug = item.slug,
                        category = item.category,
                        image = item.image,
                        price = item.price,
                        discount = item.discount,
                        unit = item.unit
                    )
                    )
                }
                updateCache(list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<List<HomeRandomProductsResponse>>> {
                return apiServices.getRandomProducts()
            }

            override fun setJob(job: Job) {
                addJob("getRandomProducts", job)
            }

        }.asLiveData()
    }

    fun getDiscountProducts():LiveData<DataState<HomeViewState>>
    {
        return object : NetworkBoundResource<HomeDiscountProductsResponse, List<HomeDiscountProducts>, HomeViewState>(
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
                    result.addSource(loadCache, Observer { homeViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = homeViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<HomeViewState> {
                return homeDao.getAllSliderImages()?.switchMap { list_images ->
                    homeDao.getAllRandomProducts()?.switchMap { list_random_products ->
                        homeDao.getAllDiscountProducts()?.switchMap { list_discount_products ->
                            object : LiveData<HomeViewState>()
                            {
                                override fun onActive() {
                                    super.onActive()
                                    value = HomeViewState(
                                        listOfSliderImage = list_images, listOfRandomProducts = list_random_products, listOfDiscountProducts = list_discount_products
                                    )
                                }
                            }
                        }?:AbsentLiveData.create()
                    }?:AbsentLiveData.create()
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<HomeDiscountProducts>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        for (item in list)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data:${item}")
                                    homeDao.insertDiscountProduct(item)
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

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<HomeDiscountProductsResponse>) {
                val list = ArrayList<HomeDiscountProducts>()
                for (item in response.body.list)
                {
                    list.add(
                        HomeDiscountProducts(
                            name = item.name,
                            get_absolute_url = item.get_absolute_url,
                            slug = item.slug,
                            category = item.category,
                            image = item.image,
                            price = item.price,
                            discount = item.discount,
                            unit = item.unit
                    )
                    )
                }
                updateCache(list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<HomeDiscountProductsResponse>> {
                return apiServices.getDiscountProducts()
            }

            override fun setJob(job: Job) {
                addJob("getDiscountProducts", job)
            }

        }.asLiveData()
    }
}