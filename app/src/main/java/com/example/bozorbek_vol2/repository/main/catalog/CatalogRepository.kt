package com.example.bozorbek_vol2.repository.main.catalog

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.model.main.catalog.parametrs.paket.Paket
import com.example.bozorbek_vol2.model.main.catalog.parametrs.product_owner.ProductOwner
import com.example.bozorbek_vol2.model.main.catalog.parametrs.sort.Sort
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.catalog.request.CatalogAddItemOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalog.CatalogResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogProduct.CatalogProductsListResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogAddOrderItemResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogViewProductListResponse
import com.example.bozorbek_vol2.persistance.main.catalog.CatalogDao
import com.example.bozorbek_vol2.repository.JobManager
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogList
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogProductList
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogViewState
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

class CatalogRepository
@Inject
constructor(
    val sessionManager: SessionManager,
    val catalogDao: CatalogDao,
    val apiServices: MainApiServices
) : JobManager("CatalogRepository") {

    var product_owner_value_id = 0
    var product_owner_value = ""
    var product_owner_parameter = ""
    var product_owner_parameter_id = 0

    var sort_value_id = 0
    var sort_value = ""
    var sort_parameter = ""
    var sort_parameter_id = 0

    var paket_value_id = 0
    var paket_value = ""
    var paket_parameter = ""
    var paket_parameter_id = 0

    fun getCatalogList(): LiveData<DataState<CatalogViewState>> {
        return object :
            NetworkBoundResource<List<CatalogResponse>, List<Catalog>, CatalogViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = false,
                cancelJobIfNoInternet = true
            ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(
                            dataState = DataState.data(
                                data = catalogViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getListOfCatalog()?.switchMap { list ->
                    object : LiveData<CatalogViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = CatalogViewState(catalogList = CatalogList(list = list))
                        }
                    }
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<Catalog>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        catalogDao.deleteAllItemCatalogProductTable()
                        for (catalog in list) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting catalog:${catalog}")
                                    catalogDao.insertCatalog(catalog)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error Inserting catalog:${catalog}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<List<CatalogResponse>>) {
                val list = ArrayList<Catalog>()
                for (catalog in response.body) {
                    list.add(
                        Catalog(
                            name = catalog.name,
                            get_absolute_url = catalog.get_absolute_url,
                            get_image = catalog.get_image,
                            slug = catalog.slug
                        )
                    )
                }

                onCompleteJob(dataState = DataState.data(data = CatalogViewState(catalogList = CatalogList(list)),response = null))
//                updateCache(cacheObject = list)
//                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<List<CatalogResponse>>> {
                return apiServices.getCatalogList()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogList", job)
            }

        }.asLiveData()
    }

    fun getCatalogProduct(slug: String): LiveData<DataState<CatalogViewState>> {
        return object :
            NetworkBoundResource<CatalogProductsListResponse, List<CatalogProduct>, CatalogViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = false,
                cancelJobIfNoInternet = false
            ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(
                            dataState = DataState.data(
                                data = catalogViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getListOfCatalogProduct()?.switchMap { list ->
                    object : LiveData<CatalogViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value =
                                CatalogViewState(catalogProductList = CatalogProductList(list = list))
                        }
                    }
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<CatalogProduct>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        catalogDao.deleteAllItemCatalogProductTable()
                        for (catalogProduct in list) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data:${catalogProduct}")
                                    catalogDao.insertCatalogProducts(catalogProduct)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error inserting data:${catalogProduct}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogProductsListResponse>) {
                val list = ArrayList<CatalogProduct>()
                for (catalogProduct in response.body.products) {
                    list.add(
                        CatalogProduct(
                            name = catalogProduct.name,
                            getAbsoluteUrl = catalogProduct.getAbsoluteUrl,
                            slug = catalogProduct.slug,
                            category = catalogProduct.category,
                            image = Constants.BASE_URL + catalogProduct.image,
                            price = catalogProduct.price,
                            discount = catalogProduct.discount,
                            unit = catalogProduct.unit
                        )
                    )
                }

                onCompleteJob(dataState = DataState.data(data = CatalogViewState(catalogProductList = CatalogProductList(list)),response = null))

//                updateCache(cacheObject = list)
//                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogProductsListResponse>> {
                return apiServices.getCatalogProductList(slug = slug)
            }

            override fun setJob(job: Job) {
                addJob("getCatalogProduct", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProduct(
        category_slug: String,
        product_slug: String
    ): LiveData<DataState<CatalogViewState>> {
        return object :
            NetworkBoundResource<CatalogViewProductListResponse, ParametersValue, CatalogViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = true,
                cancelJobIfNoInternet = true
            ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(
                            dataState = DataState.data(
                                data = catalogViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getAllCatalogViewProduct()
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: ParametersValue?) {
                cacheObject?.let { parametersValue ->
                    withContext(IO)
                    {
                        catalogDao.deleteAllSortData()
                        for (sort in parametersValue.sort) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting sort:${sort}")
                                    catalogDao.insertSortData(sort)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error inserting sort:${sort}")
                            }
                        }

                        catalogDao.deleteAllPaketDate()
                        for (paket in parametersValue.paket) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting paket:${paket}")
                                    catalogDao.insertPaket(paket)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error inserting paket:${paket}")
                            }
                        }

                        catalogDao.deleteAllProductOwnerData()
                        for (product_owner in parametersValue.productOwner) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data:${product_owner}")
                                    catalogDao.insertProductOwnerData(product_owner)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(
                                    TAG,
                                    "updateCache: Error inserting product_owner:${product_owner}"
                                )
                            }
                        }

                        catalogDao.deleteAllItemCatalogViewProductTable()
                        for (items in parametersValue.items) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting items:${items}")
                                    catalogDao.insertCatalogViewProduct(items)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error inserting items:${items}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogViewProductListResponse>) {
                var sort_list = ArrayList<Sort>()
                var paket_list = ArrayList<Paket>()
                var product_owner_list = ArrayList<ProductOwner>()

                var count = 0

                for (parameters in response.body.parameters) {
                    if (parameters.name.equals("Сорт")) {
                        for (sort in parameters.values) {
                            count++
                            sort_list.add(
                                Sort(
                                    sort_id = parameters.id,
                                    sort_name = parameters.name,
                                    sort_value_id = sort.id,
                                    sort_value = sort.value
                                )
                            )
                        }
                    } else if (parameters.name.equals("Упаковка")) {
                        for (paket in parameters.values) {
                            paket_list.add(
                                Paket(

                                    paket_id = parameters.id,
                                    paket_name = parameters.name,
                                    paket_value_id = paket.id,
                                    paket_value = paket.value
                                )
                            )
                        }
                    } else if (parameters.name.equals("Производитель")) {
                        for (product_owner in parameters.values) {
                            product_owner_list.add(
                                ProductOwner(
                                    product_owner_id = parameters.id,
                                    product_owner_name = parameters.name,
                                    product_owner_value_id = product_owner.id,
                                    product_owner_value = product_owner.value
                                )
                            )
                        }
                    }
                }

                var catalogViewProductList = ArrayList<CatalogViewProduct>()
                for (items in response.body.items) {
                    if (!items.features.isEmpty()) {
                        for (features in items.features) {
                            if (features.parameter.equals("Сорт")) {
                                sort_parameter_id = features.parameter_id
                                sort_parameter = features.parameter
                                sort_value_id = features.value_id
                                sort_value = features.value
                            } else if (features.parameter.equals("Упаковка")) {
                                paket_parameter_id = features.parameter_id
                                paket_parameter = features.parameter
                                paket_value_id = features.value_id
                                paket_value = features.value
                            } else if (features.parameter.equals("Производитель")) {
                                product_owner_parameter_id = features.parameter_id
                                product_owner_parameter = features.parameter
                                product_owner_value_id = features.value_id
                                product_owner_value = features.value
                            }
                        }

                        catalogViewProductList.add(
                            CatalogViewProduct(
                                id = items.id,
                                name = items.name,
                                form = items.form,
                                color = items.color,
                                aroma = items.aroma,
                                taste = items.taste,
                                organic = items.organic,
                                origin = items.origin,
                                piece_size = items.piece_size,
                                in_piece = items.in_piece,
                                price_in_piece = items.price_in_piece,
                                discount_in_piece = items.discount_in_piece,
                                in_gramme = items.in_gramme,
                                price_in_gramme = items.price_in_gramme * 1000,
                                discount_in_gramme = items.discount_in_gramme,
                                size_gramme = items.size_gramme,
                                size_diameter = items.size_diameter,
                                expiration = items.expiration,
                                certification = items.certification,
                                condition = items.condition,
                                storage_temp = items.storage_temp,
                                description = items.description,
                                main_image = Constants.BASE_URL + items.main_image,
                                product_name = items.product_name,
                                large = items.large,
                                large_percent = items.large_percent/100,
                                middle = items.middle,
                                middle_percent = items.middle_percent/100,
                                small = items.small,
                                small_percent = items.small_percent/100,
                                sort_parameter_id = sort_parameter_id,
                                sort_parameter = sort_parameter,
                                sort_value_id = sort_value_id,
                                sort_value = sort_value,
                                paket_parameter_id = paket_parameter_id,
                                paket_parameter = paket_parameter,
                                paket_value_id = paket_value_id,
                                paket_value = paket_value,
                                product_owner_parameter_id = product_owner_parameter_id,
                                product_owner_parameter = product_owner_parameter,
                                product_owner_value_id = product_owner_value_id,
                                product_owner_value = product_owner_value
                            )
                        )
                    }
                }

                val parametersValue = ParametersValue(
                    paket = paket_list,
                    productOwner = product_owner_list,
                    sort = sort_list,
                    items = catalogViewProductList
                )
                updateCache(parametersValue)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogViewProductListResponse>> {
                return apiServices.getCatalogViewProductList(category_slug, product_slug)
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProduct", job)
            }

        }.asLiveData()
    }

    fun getSelectedCatalogViewProduct(sortValue: String): LiveData<DataState<CatalogViewState>> {
        return object : NetworkBoundResource<Void, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(
                            dataState = DataState.data(
                                data = catalogViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductBySortValue(sortValue)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<Void>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
               addJob("getSelectedCatalogViewProduct", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductBySortAndProductOwnerValue(sortValue: String, productOwner_value:String):LiveData<DataState<CatalogViewState>>
    {
        Log.d(TAG, "getCatalogViewProductBySortAndProductOwnerValue:${productOwner_value} ")

        return object : NetworkBoundResource<Void, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ){
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductBySortAndProductOwnerValue(sort_value = sortValue, productOwner_value = productOwner_value)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            Log.d(TAG, "onActive productOwner: ${catalogViewProduct_list}")
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<Void>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
               addJob("getCatalogViewProductBySortAndProductOwnerValue", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductBySortAndProductOwnerAndPaketValue(sort_value: String, productOwner_value: String, paket_value:String):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<CatalogAddOrderItemResponse, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ){
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductBySortAndProductOwnerAndPaketValue(sort_value, productOwner_value, paket_value)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogAddOrderItemResponse>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogAddOrderItemResponse>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProductBySortAndProductOwnerAndPaketValue", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductByGramme(sort_value: String, productOwner_value: String, paket_value:String, gramme:Boolean):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<CatalogAddOrderItemResponse, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ){
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductByGramme(sort_value, productOwner_value, paket_value, gramme)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogAddOrderItemResponse>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogAddOrderItemResponse>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProductByGramme", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductByPiece(sort_value: String, productOwner_value: String, paket_value:String, piece:Boolean):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<CatalogAddOrderItemResponse, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ){
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductByPiece(sort_value, productOwner_value, paket_value, piece)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogAddOrderItemResponse>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogAddOrderItemResponse>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProductByPiece", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductBySizeLarge(sort_value: String, productOwner_value: String, paket_value:String, in_gramme:Boolean, in_piece:Boolean, large:Boolean):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<Void, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductBySizeLarge(sort_value, productOwner_value, paket_value, in_gramme, in_piece, large)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<Void>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProductBySize", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductBySizeMiddle(sort_value: String, productOwner_value: String, paket_value:String, in_gramme:Boolean, in_piece:Boolean, middle:Boolean):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<Void, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductBySizeLarge(sort_value, productOwner_value, paket_value, in_gramme, in_piece, middle)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<Void>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProductBySizeMiddle", job)
            }

        }.asLiveData()
    }

    fun getCatalogViewProductBySizeSmall(sort_value: String, productOwner_value: String, paket_value:String, in_gramme:Boolean, in_piece:Boolean, small:Boolean):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<Void, Void, CatalogViewState>(
            isNetworkRequest = false,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
                            catalogDao.getCatalogViewProductBySizeLarge(sort_value, productOwner_value, paket_value, in_gramme, in_piece, small)
                                ?.switchMap { catalogViewProduct_list ->
                                    object : LiveData<CatalogViewState>() {
                                        override fun onActive() {
                                            super.onActive()
                                            value = CatalogViewState(
                                                parametersValue = ParametersValue(
                                                    sort = sort_list,
                                                    paket = paket_list,
                                                    productOwner = product_owner_list,
                                                    items = catalogViewProduct_list
                                                )
                                            )
                                        }
                                    }
                                } ?: AbsentLiveData.create()
                        } ?: AbsentLiveData.create()
                    } ?: AbsentLiveData.create()
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<Void>) {
                TODO("Not yet implemented")
            }

            override fun createCall(): LiveData<GenericApiResponse<Void>> {
                return AbsentLiveData.create()
            }

            override fun setJob(job: Job) {
                addJob("getCatalogViewProductBySizeSmall", job)
            }

        }.asLiveData()
    }

    fun addItemCatalogViewProduct(authToken: AuthToken,product_item_id:String, quantity:Int, unit:String, size:String,sortValue: String):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<CatalogAddOrderItemResponse, Void, CatalogViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
//                withContext(Main)
//                {
//                    val loadCache = loadFromCache()
//                    result.addSource(loadCache, Observer { catalogViewState ->
//                        result.removeSource(loadCache)
//                        onCompleteJob(
//                            dataState = DataState.data(
//                                data = null,
//                                response = Response(message = "Продукт добавлен в корзину", responseType = ResponseType.Toast())
//                            )
//                        )
//                    })
//                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return AbsentLiveData.create()
//                return catalogDao.getAllSortData()?.switchMap { sort_list ->
//                    catalogDao.getAllPaketData()?.switchMap { paket_list ->
//                        catalogDao.getALlProductOwnerData()?.switchMap { product_owner_list ->
//                            catalogDao.getCatalogViewProductBySortValue(sortValue)
//                                ?.switchMap { catalogViewProduct_list ->
//                                    object : LiveData<CatalogViewState>() {
//                                        override fun onActive() {
//                                            super.onActive()
//                                            value = CatalogViewState(
//                                                parametersValue = ParametersValue(
//                                                    sort = sort_list,
//                                                    paket = paket_list,
//                                                    productOwner = product_owner_list,
//                                                    items = catalogViewProduct_list
//                                                )
//                                            )
//                                        }
//                                    }
//                                } ?: AbsentLiveData.create()
//                        } ?: AbsentLiveData.create()
//                    } ?: AbsentLiveData.create()
//                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogAddOrderItemResponse>) {
                withContext(Main)
                {
                    onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "Продукт добавлен в корзину", responseType = ResponseType.Toast())))
                }
//                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogAddOrderItemResponse>> {
                return apiServices.addOrderItem("Bearer ${authToken.access_token}", catalogAddItemOrder = CatalogAddItemOrderRequest(
                    product_item_id = product_item_id,
                    quantity = quantity,
                    unit = unit,
                    size = size
                )
                )
            }

            override fun setJob(job: Job) {
                addJob("addItemCatalogViewProduct", job)
            }

        }.asLiveData()
    }

}