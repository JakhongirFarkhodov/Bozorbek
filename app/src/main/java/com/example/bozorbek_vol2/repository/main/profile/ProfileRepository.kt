package com.example.bozorbek_vol2.repository.main.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.switchMap
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ProfileResponse
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao
import com.example.bozorbek_vol2.repository.NetworkBoundResource
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import com.example.bozorbek_vol2.util.ApiSuccessResponse
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
                        onCompleteJob(dataState = DataState.data(data = profileViewState, response = null))
                    })
                }
            }

            override fun loadFromCache(): LiveData<ProfileViewState> {
                return profileDao.getProfileData()?.switchMap { profile ->
                    object : LiveData<ProfileViewState>()
                    {
                        override fun onActive() {
                            super.onActive()
                            value = ProfileViewState(profile = profile)
                        }
                    }
                }?:AbsentLiveData.create()
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

}