package com.example.bozorbek_vol2.ui.main.basket.viewmodel

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
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
                    basketRepository.getListOfBasketProductItemOrder(authToken)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.AddAddressProductOrder ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.addAddressProductOrder(authToken = authToken, full_address = stateEvent.full_address, latitude = stateEvent.latitude, longtitude = stateEvent.longtitude)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.GetBasketAddressOrderList ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.getOrderAddressList(authToken)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.ApproveOrder ->
            {
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    basketRepository.approveOrder(authToken = authToken, address_id = stateEvent.address_id)
                }?:AbsentLiveData.create()
            }

            is BasketStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setBasketProductOrderList(list: List<BasketOrderProduct>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.basketOrderProductList.list = list
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
}