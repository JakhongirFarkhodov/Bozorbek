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
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogAddOrderItemResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogProduct.CatalogProductsListResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalog.CatalogResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogViewProductListResponse
import com.example.bozorbek_vol2.persistance.main.catalog.CatalogDao
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
import kotlin.Exception

class CatalogRepository
@Inject
constructor(
    val sessionManager: SessionManager,
    val catalogDao: CatalogDao,
    val apiServices: MainApiServices
) {
    private var repositoryJob: Job? = null

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
                updateCache(cacheObject = list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<List<CatalogResponse>>> {
                return apiServices.getCatalogList()
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getCatalogProduct(slug: String): LiveData<DataState<CatalogViewState>> {
        return object :
            NetworkBoundResource<CatalogProductsListResponse, List<CatalogProduct>, CatalogViewState>(
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
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getListOfCatalogProduct()?.switchMap { list ->
                    object : LiveData<CatalogViewState>()
                    {
                        override fun onActive() {
                            super.onActive()
                            value = CatalogViewState(catalogProductList = CatalogProductList(list = list))
                        }
                    }
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<CatalogProduct>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        for (catalogProduct in list)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data:${catalogProduct}")
                                    catalogDao.insertCatalogProducts(catalogProduct)
                                }
                            }
                            catch (e:Exception)
                            {
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

                updateCache(cacheObject = list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogProductsListResponse>> {
                return apiServices.getCatalogProductList(slug = slug)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    fun getCatalogParameters(category_slug: String, product_slug: String):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<CatalogViewProductListResponse, ParametersValue, CatalogViewState>(
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
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllPaketData()?.switchMap { paketList ->
                    catalogDao.getALlProductOwnerData()?.switchMap { productOwnerList ->
                        catalogDao.getAllSortData()?.switchMap { sortList ->
                            catalogDao.getAllCatalogViewProduct()?.switchMap { catalogViewProductList ->
                                object : LiveData<CatalogViewState>()
                                {
                                    override fun onActive() {
                                        super.onActive()
                                        value = CatalogViewState(parametersValue = ParametersValue(
                                            paket = paketList, productOwner = productOwnerList, sort = sortList, items = catalogViewProductList
                                        ))
                                    }
                                }
                            }?:AbsentLiveData.create()
                        }?:AbsentLiveData.create()
                    }?:AbsentLiveData.create()
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: ParametersValue?) {
                cacheObject?.let { parametersValue ->
                    withContext(IO)
                    {
                        catalogDao.deleteAllPaketDate()
                        for (paket in parametersValue.paket)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting paket: ${paket}")
                                    catalogDao.insertPaket(paket)
                                }.join()
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache: Error inserting paket data: ${paket}")
                            }
                        }

                        catalogDao.deleteAllProductOwnerData()
                        for (product_owner in parametersValue.productOwner)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting product_owner data: ${product_owner}")
                                    catalogDao.insertProductOwnerData(product_owner)
                                }.join()
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache: Error inserting product_owner data: ${product_owner}")
                            }
                        }

                        catalogDao.deleteAllSortData()
                        for (sort in parametersValue.sort)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting sort data: ${sort}")
                                    catalogDao.insertSortData(sort)
                                }.join()
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache: Error inserting sort data: ${sort}")
                            }
                        }

                        catalogDao.deleteAllItemCatalogViewProductTable()
                        for (item in parametersValue.items)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting items data: ${item}")
                                    catalogDao.insertCatalogViewProduct(item)
                                }.join()
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache: Error inserting items data: ${item}")
                            }
                        }


                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogViewProductListResponse>) {
                val paketList = ArrayList<Paket>()
                val product_owner_list = ArrayList<ProductOwner>()
                val sort_list = ArrayList<Sort>()
                val items_list = ArrayList<CatalogViewProduct>()


                Log.d(TAG, "handleSuccessResponse: ${response.body.items}")

                for ((index_parameter, parameter) in response.body.parameters.withIndex()){
                    for ((index_value, value) in parameter.values.withIndex())
                    {

                        if (response.body.parameters[index_parameter].id == 1)
                        {
                            sort_list.add(
                                Sort(
                                    sort_id = parameter.id,
                                    sort_name = parameter.name,
                                    sort_value = parameter.values[index_value].value,
                                    sort_value_id = parameter.values[index_value].id
                                )
                            )
                        }

                        if (response.body.parameters[index_parameter].id == 2)
                        {
                            paketList.add(Paket(
                                paket_id = parameter.id,
                                paket_name = parameter.name,
                                paket_value = parameter.values[index_value].value,
                                paket_value_id = parameter.values[index_value].id
                            ))
                        }

                        if (response.body.parameters[index_parameter].id == 3)
                        {
                            product_owner_list.add(ProductOwner(
                                product_owner_id = parameter.id,
                                product_owner_name = parameter.name,
                                product_owner_value = parameter.values[index_value].value,
                                product_owner_value_id = parameter.values[index_value].id
                            ))
                        }

                    }

                }


                for ((index_items, items) in response.body.items.withIndex())
                {
                    if (items.features.isNotEmpty())
                    {
                        Log.d(TAG, "handleSuccessResponse: ${items.id} : ${items.form}")
                        for ((index_feature, features) in items.features.withIndex())
                        {
                            if (response.body.items[index_items].features[index_feature].parameter_id == sort_list[0].sort_id)
                            {
                                sort_parameter_id = features.parameter_id
                                sort_parameter = features.parameter
                                sort_value = features.value
                                sort_value_id = features.value_id
                            }

                            if (response.body.items[index_items].features[index_feature].parameter_id == product_owner_list[0].product_owner_id)
                            {
                                product_owner_parameter_id = features.parameter_id
                                product_owner_parameter = features.parameter
                                product_owner_value = features.value
                                product_owner_value_id = features.value_id
                            }

                            if (response.body.items[index_items].features[index_feature].parameter_id == paketList[0].paket_id)
                            {
                                paket_parameter_id = features.parameter_id
                                paket_parameter = features.parameter
                                paket_value = features.value
                                paket_value_id = features.value_id
                            }
                        }

                        items_list.add(
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
                                price_in_gramme = items.price_in_gramme,
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
                                large_percent = items.large_percent,
                                middle = items.middle,
                                middle_percent = items.middle_percent,
                                small = items.small,
                                small_percent = items.small_percent,

                                product_owner_value_id = product_owner_value_id,
                                product_owner_value = product_owner_value,
                                product_owner_parameter = product_owner_parameter,
                                product_owner_parameter_id = product_owner_parameter_id,

                                sort_value_id = sort_value_id,
                                sort_value = sort_value,
                                sort_parameter = sort_parameter,
                                sort_parameter_id = sort_parameter_id,

                                paket_value_id = paket_value_id,
                                paket_value = paket_value,
                                paket_parameter = paket_parameter,
                                paket_parameter_id = paket_parameter_id


                            )
                        )
                    }
                }



                val parametersValue = ParametersValue(
                    paket = paketList,
                    productOwner = product_owner_list,
                    sort = sort_list,
                    items = items_list
                )

                updateCache(cacheObject = parametersValue)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogViewProductListResponse>> {
                return apiServices.getCatalogViewProductList(category_slug, product_slug)
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }


    fun getCatalogViewProductBySortValue(sort_value:String):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<Void, List<CatalogViewProduct>, CatalogViewState>(
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
                    result.addSource(loadCache, Observer { catalogViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = catalogViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                return catalogDao.getAllSortData()?.switchMap { sort_list ->
                    catalogDao.getCatalogViewProductBySortValue(sort_value = sort_value)?.switchMap { list ->
                        catalogDao.getAllCatalogViewProduct()?.switchMap { allCatalogList ->
                            object : LiveData<CatalogViewState>()
                            {
                                override fun onActive() {
                                    super.onActive()
                                    Log.d(TAG, "onActive: ${sort_list}")

                                    val paketList = ArrayList<Paket>()
                                    val product_owner_list = ArrayList<ProductOwner>()
//                                val sort_list = ArrayList<Sort>()

                                    for ((index, item) in list.withIndex())
                                    {
                                        paketList.add(
                                            Paket(
                                                paket_id = item.paket_parameter_id,
                                                paket_name = item.paket_parameter,
                                                paket_value = item.paket_value,
                                                paket_value_id = item.paket_value_id
                                            )
                                        )


                                        product_owner_list.add(
                                            ProductOwner(
                                                product_owner_id = item.product_owner_parameter_id,
                                                product_owner_name = item.product_owner_parameter,
                                                product_owner_value = item.product_owner_value,
                                                product_owner_value_id = item.product_owner_value_id
                                            )
                                        )

                                    }

                                    val districtPaket = paketList.toSet().toList()
                                    val districtProductOwner = product_owner_list.toSet().toList()

                                    value = CatalogViewState(parametersValue = ParametersValue(items = allCatalogList, paket = districtPaket, productOwner = districtProductOwner, sort = sort_list))
                                }
                            }
                        }?:AbsentLiveData.create()
                    }?:AbsentLiveData.create()
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<CatalogViewProduct>?) {
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


    fun addOrderItem(authToken: AuthToken, product_item_id:String, quantity:Int, unit:String):LiveData<DataState<CatalogViewState>>
    {
        return object : NetworkBoundResource<CatalogAddOrderItemResponse, Void, CatalogViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<CatalogViewState> {
                TODO("Not yet implemented")
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<CatalogAddOrderItemResponse>) {
                withContext(Main)
                {
                    Log.d(TAG, "handleSuccessResponse: ${response.body.message}")
                    onCompleteJob(dataState = DataState.data(data = CatalogViewState(message = response.body.message),response = Response(message = response.body.message, responseType = ResponseType.Dialog())))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<CatalogAddOrderItemResponse>> {
                return apiServices.addOrderItem(token = "Bearer ${authToken.access_token}", CatalogAddItemOrderRequest(product_item_id = product_item_id, quantity = quantity, unit = unit))
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

}