package com.example.bozorbek_vol2.repository.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackages
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileUpdatePasswordRequest
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ProfileResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ProfileUpdatePasswordResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages.ProfileAllReadyPackagesResponse
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.Response
import com.example.bozorbek_vol2.ui.ResponseType
import com.example.bozorbek_vol2.ui.auth.state.AuthViewState
import com.example.bozorbek_vol2.ui.auth.state.PasswordValue
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
import javax.inject.Inject

class ProfileRepository
@Inject
constructor(
    val sessionManager: SessionManager,
    val apiServices: MainApiServices,
    val profileDao: ProfileDao
) {
    private var repositoryJob: Job? = null
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
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }


    fun getAllReadyPackages(auth_token: AuthToken): LiveData<DataState<ProfileViewState>> {
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
                        onCompleteJob(dataState = DataState.data(data = profileViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return profileDao.getProfileReadyPackages()?.switchMap { list_package ->

                        object : LiveData<ProfileViewState>()
                        {
                            override fun onActive() {
                                super.onActive()
                                value = ProfileViewState(readyPackagesList = list_package)
                            }
                        }

                }?:AbsentLiveData.create()
            }

            override suspend fun updateCache(cacheObject: List<ProfileReadyPackages>?) {
                cacheObject?.let { profileReadyPackageData ->
                    withContext(IO)
                    {
                        profileDao.deleteProfileReadyPackages()
                        for (item in profileReadyPackageData)
                        {
                            try {
                                launch {
                                    Log.d(TAG, "updateCache: Inserting ready packages: ${item}")
                                    profileDao.insertProfileReadyPackages(item)
                                }.join()
                            }
                            catch (e:Exception)
                            {
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
                return apiServices.getAllReadyPackages("Bearer ${auth_token.access_token}")
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }


    fun updateProfilePassword(auth_token: AuthToken, old_password:String, new_password:String, confirm_password:String):LiveData<DataState<ProfileViewState>>
    {
        val passwordError = ProfilePasswordValue(old_password = old_password, password = new_password, confirm_password = confirm_password).checkPasswordValue()
        if (!passwordError.equals(ProfilePasswordValue.PasswordError.none()))
        {
            Log.d(Constants.LOG, "updateProfilePassword: Error:${passwordError}")
            return onErrorFields(error_message = passwordError, responseType = ResponseType.Dialog())
        }

        return object : NetworkBoundResource<ProfileUpdatePasswordResponse, Void, ProfileViewState>(
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

            override suspend fun updateCache(cacheObject: Void?) {
                TODO("Not yet implemented")
            }

            override suspend fun handleSuccessResponse(response: ApiSuccessResponse<ProfileUpdatePasswordResponse>) {
                withContext(Main)
                {
                    if (response.body.message.equals("Success")) {
                        onCompleteJob(dataState = DataState.data(data = null, response = Response(message = "Пароль успешно обнавлен", responseType = ResponseType.Toast())))
                    }
                }
            }

            override fun createCall(): LiveData<GenericApiResponse<ProfileUpdatePasswordResponse>> {
                return apiServices.updateProfilePassword(token = "Bearer ${auth_token.access_token}",
                profileUpdatePasswordRequest = ProfileUpdatePasswordRequest(password = old_password, new_password = confirm_password)
                )
            }

            override fun setJob(job: Job) {
                repositoryJob?.cancel()
                repositoryJob = job
            }

        }.asLiveData()
    }

    private fun onErrorFields(error_message:String, responseType: ResponseType): LiveData<DataState<ProfileViewState>> {
        return object : LiveData<DataState<ProfileViewState>>()
        {
            override fun onActive() {
                super.onActive()
                value = DataState.error(response = Response(message = error_message, responseType = responseType))
            }
        }
    }

}