package com.example.bozorbek_vol2.ui.main.basket.viewmodel

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.network.main.network_services.basket.response.GetBasketListAddressResponse
import com.example.bozorbek_vol2.repository.main.basket.BasketRepository
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.basket.state.BasketStateEvent
import com.example.bozorbek_vol2.ui.main.basket.state.BasketViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import javax.inject.Inject

class BasketViewModel
    @Inject
    constructor(
        val basketRepository: BasketRepository,
        val sessionManager: SessionManager
    )
    : BaseViewModel<BasketStateEvent, BasketViewState>() {
    override fun initNewViewState(): BasketViewState {
        return BasketViewState()
    }

    override fun handleStateEvent(stateEvent: BasketStateEvent): LiveData<DataState<BasketViewState>> {
        when(stateEvent)
        {
            is BasketStateEvent.GetBasketProductOrderList ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.getBasketProductOrderList(authToken)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.GetBasketProfileInfo -> {
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.getProfileInfo(authToken)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.RemoveBasketOrderProductById ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.removeBasketProductById(authToken = authToken, item_id = stateEvent.product_item_id)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.AddAddressProductOrder ->{
                return sessionManager.cachedAuthToken.value?.let { auth_token ->
                    basketRepository.setOrderAddress(authToken = auth_token, full_address = stateEvent.full_address, latitude = stateEvent.latitude, longtitude = stateEvent.longtitude)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.GetBasketAddressOrderList ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.getAddressList(authToken = authToken)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.ApproveOrder ->
            {
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.approveBasketOrder(authToken = authToken, address_id = stateEvent.address_id, name = stateEvent.name, phone_num = stateEvent.phone_num, comment = stateEvent.comment)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.SetCreatedReadyPackage ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.setCreatedReadyPackage(authToken, stateEvent.saveReadyPackageRequest)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.None ->{
                return object : LiveData<DataState<BasketViewState>>()
                {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null, null)
                    }
                }
            }
        }
    }

    fun setBasketProductOrderList(list: List<BasketOrderProduct>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.basketOrderProductList!!.list = list
        _viewState.value = update
    }

    fun setProfileInfo(profile: Profile)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.profile == profile)
        {
            return
        }
        update.profile = profile
        _viewState.value = update
    }

    fun setBasketAddAddressMessage(message:String)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.basketGetAddedAddressMessage == message)
        {
            return
        }
        update.basketGetAddedAddressMessage = message
        _viewState.value = update
    }

    fun setBasketAddressOrderProductList(list: List<GetBasketListAddressResponse>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.basketGetAddressOrderList.list = list
        _viewState.value = update
    }

    fun handlePendingData()
    {
        setStateEvent(BasketStateEvent.None())
    }

    fun cancelActiveJob()
    {
        handlePendingData()
        basketRepository.cancelActiveJob()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }
}