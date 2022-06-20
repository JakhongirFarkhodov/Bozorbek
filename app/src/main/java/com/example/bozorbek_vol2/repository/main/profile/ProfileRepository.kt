package com.example.bozorbek_vol2.repository.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.profile.*
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileComplaintsRequest
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileUpdatePasswordRequest
import com.example.bozorbek_vol2.network.main.network_services.profile.response.*
import com.example.bozorbek_vol2.network.main.network_services.profile.response.active_order.ProfileActiveOrHistoryOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_package_id.ReadyPackageIdResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages.ProfileAllReadyPackagesAddItemToBasketResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages.ProfileAllReadyPackagesResponse
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao
import com.example.bozorbek_vol2.repository.JobManager
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.main.profile.state.ProfilePasswordValue
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
import okhttp3.MultipartBody
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val sessionManager: SessionManager,
    val apiServices: MainApiServices,
    val profileDao: ProfileDao
) : JobManager("ProfileRepository") {


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

    fun getProfileInfo(auth_token: AuthToken): LiveData<DataState<ProfileViewState>> {
        return object : NetworkBoundResource<ProfileResponse, Profile, ProfileViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = true,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { profileViewState ->
                        onCompleteJob(
                            dataState = DataState.data(
                                data = profileViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return profileDao.getProfileData()?.switchMap { profile ->
                    object : LiveData<ProfileViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = ProfileViewState(profile = profile)
                        }
                    }
                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Profile?) {
                cacheObject?.let { profile ->
                    withContext(IO)
                    {
                        try {
                            launch {
                                profileDao.insertProfileData(profile = profile)
                            }
                        } catch (e: Exception) {
                            Log.d(TAG, "updateCache: Error insertin profile data")
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileResponse>) {
                Log.d(TAG, "handleSuccessResponse: ${response.body}")
                val profile = Profile(
                    username = response.body.username,
                    customer_phone = response.body.customerPhone,
                    first_name = response.body.firstName,
                    last_name = response.body.lastName,
                    get_image = response.body.getImage
                )

                updateCache(profile)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileResponse>> {
                return apiServices.getProfileInfo("Bearer ${auth_token.access_token}")
            }

            override fun setJob(job: Job) {
                addJob("getProfileInfo", job)
            }

        }.asLiveData()
    }


    fun getAllReadyPackages(auth_token: AuthToken, type:String): LiveData<DataState<ProfileViewState>> {
        return object :
            NetworkBoundResource<ProfileAllReadyPackagesResponse, List<ProfileReadyPackages>, ProfileViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = true,
                cancelJobIfNoInternet = true
            ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { profileViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(
                            dataState = DataState.data(
                                data = profileViewState,
                                response = null
                            )
                        )
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return profileDao.getProfileReadyPackages()?.switchMap { list_package ->

                    object : LiveData<ProfileViewState>() {
                        override fun onActive() {
                            super.onActive()
                            value = ProfileViewState(readyPackagesList = list_package)
                        }
                    }

                } ?: AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<ProfileReadyPackages>?) {
                cacheObject?.let { profileReadyPackageData ->
                    withContext(IO)
                    {
                        profileDao.deleteProfileReadyPackages()
                        for (item in profileReadyPackageData) {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting ready packages: ${item}")
                                    profileDao.insertProfileReadyPackages(item)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "updateCache: Error inserting ready data: ${item}")
                            }
                        }

                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileAllReadyPackagesResponse>) {
                val ready_packages_list = ArrayList<ProfileReadyPackages>()
                val readyPackages_category_list = ArrayList<ProfileReadyPackages>()
                var count = 0
                for ((ready_package_index, ready_package) in response.body.results.withIndex()) {
                    for ((category_index, category) in response.body.results[ready_package_index].categories.withIndex()) {
                        count++
                        ready_packages_list.add(
                            ProfileReadyPackages(
                                package_id = count,
                                id = ready_package.id,
                                package_name = ready_package.name,
                                author = ready_package.author,
                                visibility = ready_package.visibility,
                                total_cost = ready_package.total_cost,
                                items_count = ready_package.items_count,
                                category_name = category.name,
                                get_absolute_url = category.get_absolute_url,
                                get_image = Constants.BASE_URL + category.get_image,
                                slug = category.slug
                            )
                        )


                    }
                }


                updateCache(ready_packages_list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileAllReadyPackagesResponse>> {
                return apiServices.getAllReadyPackages("Bearer ${auth_token.access_token}", type = type)
            }

            override fun setJob(job: Job) {
                addJob("getAllReadyPackages", job)
            }

        }.asLiveData()
    }


    fun updateProfilePassword(
        auth_token: AuthToken,
        old_password: String,
        new_password: String,
        confirm_password: String
    ): LiveData<DataState<ProfileViewState>> {
        val passwordError = ProfilePasswordValue(
            old_password = old_password,
            password = new_password,
            confirm_password = confirm_password
        ).checkPasswordValue()
        if (!passwordError.equals(ProfilePasswordValue.PasswordError.none())) {
            Log.d(Constants.LOG, "updateProfilePassword: Error:${passwordError}")
            return onErrorFields(
                error_message = passwordError,
                responseType = ResponseType.Dialog()
            )
        }

        return object : NetworkBoundResource<ProfileUpdatePasswordResponse, Void, ProfileViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileUpdatePasswordResponse>) {
                withContext(Main)
                {
                    if (response.body.message.equals("Success")) {
                        onCompleteJob(
                            dataState = DataState.data(
                                data = null,
                                response = Response(
                                    message = "Пароль успешно обнавлен",
                                    responseType = ResponseType.Toast()
                                )
                            )
                        )
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileUpdatePasswordResponse>> {
                return apiServices.updateProfilePassword(
                    token = "Bearer ${auth_token.access_token}",
                    profileUpdatePasswordRequest = ProfileUpdatePasswordRequest(
                        password = old_password,
                        new_password = confirm_password
                    )
                )
            }

            override fun setJob(job: Job) {
                addJob("updateProfilePassword", job)
            }

        }.asLiveData()
    }

    fun addItemReadyPackageToBasket(
        auth_token: AuthToken,
        ready_package_id: String
    ): LiveData<DataState<ProfileViewState>> {
        return object :
            NetworkBoundResource<ProfileAllReadyPackagesAddItemToBasketResponse, Void, ProfileViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = false,
                cancelJobIfNoInternet = true
            ) {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileAllReadyPackagesAddItemToBasketResponse>) {
                withContext(IO)
                {
                    if (response.body.message.equals("Success")) {
                        onCompleteJob(
                            dataState = DataState.data(
                                data = null,
                                response = Response(
                                    message = "Пакет добавлен в корзину",
                                    responseType = ResponseType.Toast()
                                )
                            )
                        )
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileAllReadyPackagesAddItemToBasketResponse>> {
                return apiServices.addItemReadyPackageToBasket(
                    token = "Bearer ${auth_token.access_token}",
                    ready_package_id = ready_package_id
                )
            }

            override fun setJob(job: Job) {
                addJob("addItemReadyPackageToBasket", job)
            }

        }.asLiveData()
    }

    fun uploadProfileImage(
        auth_token: AuthToken,
        image: MultipartBody.Part?
    ): LiveData<DataState<ProfileViewState>> {
        return object : NetworkBoundResource<ProfileUploadImageResponse, Void, ProfileViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        ) {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileUploadImageResponse>) {
                withContext(Main)
                {
                    onCompleteJob(
                        dataState = DataState.data(
                            data = null,
                            response = Response(
                                message = "Фото обнавлено",
                                responseType = ResponseType.Toast()
                            )
                        )
                    )
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileUploadImageResponse>> {
                return apiServices.uploadUserImage(
                    token = "Bearer ${auth_token.access_token}",
                    image = image
                )
            }

            override fun setJob(job: Job) {
                addJob("uploadProfileImage", job)
            }

        }.asLiveData()
    }

    fun getProfileActiveOrHistoryOrder(
        auth_token: AuthToken,
        UNAPPROVED: String?,
        APPROVED: String?,
        COLLECTING: String?,
        COLLECTED: String?,
        DELIVERING: String?,
        DELIVERED: String?,
        CANCELLED: String?
    ): LiveData<DataState<ProfileViewState>> {
        return object :
            NetworkBoundResource<List<ProfileActiveOrHistoryOrderResponse>, List<ProfileActiveOrHistoryOrder>, ProfileViewState>(
                isNetworkRequest = true,
                isNetworkAvailable = sessionManager.isInternetAvailable(),
                shouldUseCacheObject = true,
                cancelJobIfNoInternet = true
            ) {
            override suspend fun createCacheAndReturn() {
                withContext(Main)
                {
                    val loadCache = loadFromCache()
                    result.addSource(loadCache, Observer { profileViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(DataState.data(data = profileViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return if(DELIVERED.isNullOrBlank() || CANCELLED.isNullOrBlank()){
                    profileDao.getActiveOrderData(UNAPPROVED!!, APPROVED!!, COLLECTING!!, COLLECTED!!, DELIVERING!!)?.switchMap { list ->
                        object : LiveData<ProfileViewState>() {
                            override fun onActive() {
                                super.onActive()
                                value = ProfileViewState(profileActiveOrHistoryOrder = list)
                            }
                        }
                    } ?: AbsentLiveData.create()
                }else{
                    return profileDao.getHistoryOrderData(
                        DELIVERED = DELIVERED,
                        CANCELLED = CANCELLED
                    )?.switchMap { list ->
                        object : LiveData<ProfileViewState>() {
                            override fun onActive() {
                                super.onActive()
                                Log.d(TAG, "onActiveOrder: ${list}")
                                value = ProfileViewState(profileActiveOrHistoryOrder = list)
                            }
                        }
                    } ?: AbsentLiveData.create()
                }
            }

            override suspend fun updateCache(cacheObject: List<ProfileActiveOrHistoryOrder>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        profileDao.deleteAllActiveOrHistoryOrderItem()
                        for (item in list) {
                            try {
                                launch {
                                    Log.d(
                                        TAG,
                                        " inserting ProfileActiveOrHistoryOrder data: ${item}"
                                    )
                                    profileDao.insertAllActiveOrHistoryOrderItem(item)
                                }.join()
                            } catch (e: Exception) {
                                Log.d(TAG, "error inserting ProfileActiveOrHistoryOrder: ${item}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<List<ProfileActiveOrHistoryOrderResponse>>) {
                val list = ArrayList<ProfileActiveOrHistoryOrder>()
                var count: Int = 0
                for (item in response.body.withIndex()) {
                    for (product_item in item.value.items) {
                        if (!product_item.product_item.features.isEmpty()) {
                            for (features in product_item.product_item.features) {
                                if (features.parameter_id == 1) {
                                    sort_parameter_id = features.parameter_id
                                    sort_parameter = features.parameter
                                    sort_value_id = features.value_id
                                    sort_value = features.value
                                } else if (features.parameter_id == 2) {
                                    paket_parameter_id = features.parameter_id
                                    paket_parameter = features.parameter
                                    paket_value_id = features.value_id
                                    paket_value = features.value
                                } else if (features.parameter_id == 3) {
                                    product_owner_parameter_id = features.parameter_id
                                    product_owner_parameter = features.parameter
                                    product_owner_value_id = features.value_id
                                    product_owner_value = features.value
                                }
                            }
//                            count++
                            list.add(
                                ProfileActiveOrHistoryOrder(
                                    id = item.value.id,
                                    address = item.value.address.toInt(),
                                    created_at = item.value.created_at,
                                    status = item.value.status,
                                    user_approved = item.value.user_approved,
                                    total_cost = item.value.total_cost.toFloat(),
                                    delivery_cost = item.value.delivery_cost.toFloat(),
                                    collect_time = item.value.collect_time,
                                    delivery_time = item.value.collect_time,
                                    client_phone = item.value.client_phone,
                                    client_name = item.value.client_name,
                                    item_id = product_item.id,
                                    item_price = product_item.price.toFloat(),
                                    item_unit = product_item.unit,
                                    item_quantity = product_item.quantity,
                                    product_item_id = product_item.product_item.id,
                                    name = product_item.product_item.name,
                                    form = product_item.product_item.form,
                                    color = product_item.product_item.color,
                                    aroma = product_item.product_item.aroma,
                                    taste = product_item.product_item.taste,
                                    organic = product_item.product_item.organic,
                                    origin = product_item.product_item.origin,
                                    piece_size = product_item.product_item.piece_size,
                                    in_piece = product_item.product_item.in_piece,
                                    price_in_piece = product_item.product_item.price_in_piece.toFloat(),
                                    discount_in_piece = product_item.product_item.discount_in_piece,
                                    in_gramme = product_item.product_item.in_gramme,
                                    price_in_gramme = product_item.product_item.price_in_gramme,
                                    discount_in_gramme = product_item.product_item.discount_in_gramme,
                                    size_gramme = product_item.product_item.size_diameter,
                                    size_diameter = product_item.product_item.size_diameter,
                                    expiration = product_item.product_item.expiration,
                                    certification = product_item.product_item.certification,
                                    condition = product_item.product_item.condition,
                                    storage_temp = product_item.product_item.storage_temp,
                                    description = product_item.product_item.description,
                                    main_image = product_item.product_item.main_image,
                                    product_name = product_item.product_item.product_name,
                                    large = product_item.product_item.large,
                                    large_percent = product_item.product_item.large_percent,
                                    middle = product_item.product_item.middle,
                                    middle_percent = product_item.product_item.middle_percent,
                                    small = product_item.product_item.small,
                                    small_percent = product_item.product_item.small_percent,
                                    sort_value = sort_value,
                                    sort_value_id = sort_value_id,
                                    sort_parameter = sort_parameter,
                                    sort_parameter_id = sort_parameter_id,
                                    product_owner_value = product_owner_value,
                                    product_owner_value_id = product_owner_value_id,
                                    product_owner_parameter = product_owner_parameter,
                                    product_owner_parameter_id = product_owner_parameter_id,
                                    paket_value = paket_value,
                                    paket_value_id = paket_value_id,
                                    paket_parameter = paket_parameter,
                                    paket_parameter_id = paket_parameter_id
                                )
                            )

                        }

                    }
                }
                updateCache(cacheObject = list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<List<ProfileActiveOrHistoryOrderResponse>>> {
                return apiServices.getActiveOrHistoryOrder(accessToken = "Bearer ${auth_token.access_token}")
            }

            override fun setJob(job: Job) {
                addJob("getProfileActiveOrHistoryOrder", job)
            }

        }.asLiveData()
    }


    fun setComplaints(auth_token: AuthToken, title:String, text:String):LiveData<DataState<ProfileViewState>>
    {
        return object : NetworkBoundResource<ProfileComplaintsResponse, Any, ProfileViewState>(
            isNetworkRequest = true,
            isNetworkAvailable = sessionManager.isInternetAvailable(),
            shouldUseCacheObject = false,
            cancelJobIfNoInternet = true
        )
        {
            override suspend fun createCacheAndReturn() {
                TODO("Not yet implemented")
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: Any?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileComplaintsResponse>) {
                withContext(Main)
                {
                    onCompleteJob(DataState.data(data = null, response = Response(message = "Ваша жалаба отправлено", responseType = ResponseType.Toast())))
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileComplaintsResponse>> {
                return apiServices.setComplaint(token = "Bearer ${auth_token.access_token}", profileComplaints = ProfileComplaintsRequest(
                    title, text
                ))
            }

            override fun setJob(job: Job) {
                addJob("setComplaints", job)
            }

        }.asLiveData()
    }

    fun getNotification(auth_token: AuthToken):LiveData<DataState<ProfileViewState>>
    {
        return object : NetworkBoundResource<List<ProfileNotificationResponse>, List<ProfileNotification>, ProfileViewState>(
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
                    result.addSource(loadCache, Observer { profileViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = profileViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return profileDao.getAllNotification()?.switchMap {list ->
                    object : LiveData<ProfileViewState>()
                    {
                        override fun onActive() {
                            super.onActive()
                            value = ProfileViewState(profileNotificationList = list)
                        }
                    }
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<ProfileNotification>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        for (item in list)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting data ${item}")
                                    profileDao.insertNotification(item)
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

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<List<ProfileNotificationResponse>>) {
                val notification_list = ArrayList<ProfileNotification>()
                for (item in response.body)
                {
                    notification_list.add(
                        ProfileNotification(
                        id = item.id,
                        title = item.title,
                        content = item.content,
                        to_customer = item.to_customer,
                        created_at = item.created_at,
                        is_read = item.is_read
                    )
                    )
                }

                updateCache(notification_list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<List<ProfileNotificationResponse>>> {
                return apiServices.getNotifications(token = "Bearer ${auth_token.access_token}")
            }

            override fun setJob(job: Job) {
                addJob("getNotification", job)
            }

        }.asLiveData()
    }

    fun getReadyPackageById(auth_token: AuthToken, id:Int):LiveData<DataState<ProfileViewState>>
    {
        return object : NetworkBoundResource<ReadyPackageIdResponse, List<ProfileReadyPackageId>,ProfileViewState>(
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
                    result.addSource(loadCache, Observer { profileViewState ->
                        result.removeSource(loadCache)
                        onCompleteJob(dataState = DataState.data(data = profileViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return profileDao.getProfileReadyPackageId()?.switchMap { list ->
                    object : LiveData<ProfileViewState>()
                    {
                        override fun onActive() {
                            super.onActive()
                            value = ProfileViewState(profileReadyPackageIdList = list)
                        }
                    }
                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<ProfileReadyPackageId>?) {
                cacheObject?.let { list ->
                    withContext(IO)
                    {
                        profileDao.deleteAllProfileReadyPackageId()
                        for (item in list)
                        {
                            try {
                                Log.d(TAG, "updateCache: Inserting readyPackageId data:${item}")
                                profileDao.insertProfileReadyPackageId(item)
                            }
                            catch (e:Exception)
                            {
                                Log.d(TAG, "updateCache Error inserting readyPackageId data: ${item}")
                            }
                        }
                    }
                }
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ReadyPackageIdResponse>) {
                val list = ArrayList<ProfileReadyPackageId>()
                for (item in response.body.items)
                {

                        list.add(
                            ProfileReadyPackageId(
                                id = item.product_item.id,
                                name = item.product_item.name,
                                form = item.product_item.form,
                                color = item.product_item.color,
                                aroma = item.product_item.aroma,
                                taste = item.product_item.taste,
                                organic = item.product_item.organic,
                                origin = item.product_item.origin,
                                piece_size = item.product_item.piece_size,
                                in_piece = item.product_item.in_piece,
                                price_in_piece = item.product_item.price_in_piece.toFloat(),
                                discount_in_piece = item.product_item.discount_in_piece,
                                in_gramme = item.product_item.in_gramme,
                                price_in_gramme = item.product_item.price_in_gramme * 1000,
                                discount_in_gramme = item.product_item.discount_in_gramme,
                                size_gramme = item.product_item.size_gramme,
                                size_diameter = item.product_item.size_diameter,
                                expiration = item.product_item.expiration,
                                certification = item.product_item.certification,
                                condition = item.product_item.condition,
                                storage_temp = item.product_item.storage_temp,
                                description = item.product_item.description,
                                main_image = Constants.BASE_URL + item.product_item.main_image,
                                product_name = item.product_item.product_name,
                                large = item.product_item.large,
                                large_percent = item.product_item.large_percent,
                                middle = item.product_item.middle,
                                middle_percent = item.product_item.middle_percent,
                                small = item.product_item.small,
                                small_percent = item.product_item.small_percent,
                                quantity = item.quantity,
                                unit = item.unit,
                                size = item.size,
                                price = item.price
                        )
                        )

                }

                updateCache(list)
                createCacheAndReturn()
            }

            override fun createCall(): LiveData<GenericApiResponse<ReadyPackageIdResponse>> {
                return apiServices.getReadyPackageById(
                    token = "Bearer ${auth_token.access_token}",
                    ready_package_id = id
                )
            }

            override fun setJob(job: Job) {
                addJob("getReadyPackageById", job)
            }

        }.asLiveData()
    }

    private fun onErrorFields(
        error_message: String,
        responseType: ResponseType
    ): LiveData<DataState<ProfileViewState>> {
        return object : LiveData<DataState<ProfileViewState>>() {
            override fun onActive() {
                super.onActive()
                value = DataState.error(
                    response = Response(
                        message = error_message,
                        responseType = responseType
                    )
                )
            }
        }
    }

}