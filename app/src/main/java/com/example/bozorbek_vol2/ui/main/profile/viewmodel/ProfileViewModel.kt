package com.example.bozorbek_vol2.ui.main.profile.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.profile.*
import com.example.bozorbek_vol2.network.main.network_services.search.response.SearchHistoryResponse
import com.example.bozorbek_vol2.repository.main.profile.ProfileRepository
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileStateEvent
import com.example.bozorbek_vol2.ui.main.profile.state.ProfileViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import javax.inject.Inject

class ProfileViewModel
    @Inject
    constructor(val sessionManager: SessionManager, val profileRepository: ProfileRepository, val sharedPreferences: SharedPreferences, val editor: SharedPreferences.Editor)
    : BaseViewModel<ProfileStateEvent, ProfileViewState>() {
    override fun initNewViewState(): ProfileViewState {
        return ProfileViewState()
    }

    override fun handleStateEvent(stateEvent: ProfileStateEvent): LiveData<DataState<ProfileViewState>> {
        when(stateEvent)
        {
            is ProfileStateEvent.GetProfileInfo ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getProfileInfo(authToken)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.UploadProfileImage ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.uploadProfileImage(authToken, stateEvent.image)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.GetProfileReadyPackages ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getAllReadyPackages(auth_token = authToken, type = stateEvent.type)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.SetReadyPackageToAutoOrder ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.setReadyPackageToAutoOrder(auth_token = authToken, stateEvent.profileReadyPackageAutoOrder)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.GetSearchHistory -> {
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getSearchHistory(authToken)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.GetAllActiveOrHistoryOrder ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getProfileActiveOrHistoryOrder(authToken,
                    UNAPPROVED = stateEvent.UNAPPROVED,
                        APPROVED = stateEvent.APPROVED,
                        COLLECTING = stateEvent.COLLECTING,
                        COLLECTED = stateEvent.COLLECTED,
                        DELIVERED = stateEvent.DELIVERED,
                        DELIVERING = stateEvent.DELIVERING,
                        CANCELLED = stateEvent.CANCELLED
                        )
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.SetComplaints ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.setComplaints(authToken, title = stateEvent.title, text = stateEvent.text)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.SetReadyPackageId ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getReadyPackageById(
                        auth_token = authToken,
                        id = stateEvent.id
                    )
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.AddItemReadyPackagesToBasket ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.addItemReadyPackageToBasket(auth_token = authToken, ready_package_id = stateEvent.package_item_id)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.UpdateProfilePassword ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.updateProfilePassword(auth_token = authToken, old_password = stateEvent.old_password, new_password = stateEvent.new_password, confirm_password = stateEvent.confirm_new_password)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.SetNotificationEvent ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getNotification(authToken)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.RemoveReadyPackageItem ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.deleteReadyPackageById(authToken, stateEvent.id)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.GetProfileAutoOrderList ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getAutoOrderItem(authToken)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.None ->{
                return object : LiveData<DataState<ProfileViewState>>()
                {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null, null)
                    }
                }
            }
        }
    }

    fun setProfileData(profile:Profile)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.profile == profile)
        {
            return
        }
        update.profile = profile
        _viewState.value = update
    }

    fun setProfileAllPackagesList(profileReadyPackagesList: List<ProfileReadyPackages>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.readyPackagesList = profileReadyPackagesList
        _viewState.value = update
    }

    fun setProfileAllActiveOrHistoryOrder(list: List<ProfileActiveOrHistoryOrder>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.profileActiveOrHistoryOrder = list
        _viewState.value = update
    }

    fun setNotificationList(list:List<ProfileNotification>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.profileNotificationList = list
        _viewState.value = update
    }

    fun setProfileReadyPackageIdList(list:List<ProfileReadyPackageId>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.profileReadyPackageIdList = list
        _viewState.value = update
    }

    fun setAutoOrderList(list:List<ProfileAutoOrder>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.profileAutoOrderList = list
        _viewState.value = update
    }

    fun setSearchHistoryList(list:List<SearchHistoryResponse>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.searchHistoryOrder = list
        _viewState.value = update
    }


    fun handlingPendingData()
    {
        setStateEvent(ProfileStateEvent.None())
    }

    fun cancelActiveJob()
    {
        handlingPendingData()
        profileRepository.cancelActiveJob()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }

}