package com.example.bozorbek_vol2.ui.main.profile.viewmodel

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackages
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
    constructor(val sessionManager: SessionManager, val profileRepository: ProfileRepository)
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

            is ProfileStateEvent.GetProfileReadyPackages ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.getAllReadyPackages(auth_token = authToken)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.UpdateProfilePassword ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    profileRepository.updateProfilePassword(auth_token = authToken, old_password = stateEvent.old_password, new_password = stateEvent.new_password, confirm_password = stateEvent.confirm_new_password)
                }?:AbsentLiveData.create()
            }

            is ProfileStateEvent.None ->{
                return AbsentLiveData.create()
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


}