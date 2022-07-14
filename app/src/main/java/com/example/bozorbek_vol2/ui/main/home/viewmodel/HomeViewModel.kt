package com.example.bozorbek_vol2.ui.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import com.bumptech.glide.RequestManager
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.model.main.home.HomeDiscountProducts
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.model.main.home.HomeSliderImage
import com.example.bozorbek_vol2.repository.main.home.HomeRepository
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.home.state.HomeStateEvent
import com.example.bozorbek_vol2.ui.main.home.state.HomeStateEvent.*
import com.example.bozorbek_vol2.ui.main.home.state.HomeViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import javax.inject.Inject

class HomeViewModel
    @Inject
    constructor(val sessionManager: SessionManager, val homeRepository: HomeRepository, val requestManager: RequestManager)
    : BaseViewModel<HomeStateEvent, HomeViewState>() {
    override fun initNewViewState(): HomeViewState {
        return HomeViewState()
    }

    override fun handleStateEvent(stateEvent: HomeStateEvent): LiveData<DataState<HomeViewState>> {
        when(stateEvent)
        {
            is GetHomeSliderImage ->{
                Log.d(TAG, "GetHomeSliderImage triggered: ")
                return homeRepository.getAllSliderImages()
            }

            is GetRandomProducts ->{
                return homeRepository.getRandomProducts()
            }

            is GetDiscountProducts ->{
                return homeRepository.getDiscountProducts()
            }

            is GetCatalogViewProductListOfData ->{
                return homeRepository.getCatalogViewProduct(category_slug = stateEvent.category_slug, product_slug = stateEvent.product_slug)
            }

            is GetCatalogViewProductBySortValue -> {
                Log.d(TAG, "GetCatalogViewProductBySortValue:${stateEvent.sort_value} ")
                return homeRepository.getSelectedCatalogViewProduct(sortValue = stateEvent.sort_value)
            }

            is GetCatalogViewProductBySortAndProductOwnerValue ->{
                return homeRepository.getCatalogViewProductBySortAndProductOwnerValue(stateEvent.sort_value, stateEvent.productOwner_value)
            }

            is GetCatalogViewProductBySortAndProductOwnerAndPaketValue ->{
                return homeRepository.getCatalogViewProductBySortAndProductOwnerAndPaketValue(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value)
            }

            is GetCatalogViewProductByGramme ->{
                return homeRepository.getCatalogViewProductByGramme(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme)
            }

            is GetCatalogViewProductByPiece ->{
                return homeRepository.getCatalogViewProductByPiece(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.piece)
            }

            is GetCatalogViewProductBySizeLarge ->{
                return homeRepository.getCatalogViewProductBySizeLarge(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.large)
            }
            is GetCatalogViewProductBySizeMiddle ->{
                return homeRepository.getCatalogViewProductBySizeMiddle(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.middle)
            }
            is GetCatalogViewProductBySizeSmall ->{
                return homeRepository.getCatalogViewProductBySizeSmall(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.small)
            }
            is AddCatalogOrderItem ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    homeRepository.addItemCatalogViewProduct(
                        authToken = authToken,
                        product_item_id = stateEvent.product_item_id,
                        quantity = stateEvent.quantity,
                        unit = stateEvent.unit,
                        size = stateEvent.size,
                        sortValue = stateEvent.sortValue)
                }?: AbsentLiveData.create()
            }


            is None ->{
                return object : LiveData<DataState<HomeViewState>>()
                {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null, null)
                    }
                }
            }
        }
    }

    fun setParametersValue(parametersValue: ParametersValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.parametersValue = parametersValue
        _viewState.value = update
    }


    fun setHomeSliderImage(list:List<HomeSliderImage>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.listOfSliderImage = list
        _viewState.value = update
    }

    fun setHomeRandomProduct(list:List<HomeRandomProducts>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.listOfRandomProducts = list
        _viewState.value = update
    }

    fun setHomeDiscountProduct(list:List<HomeDiscountProducts>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.listOfDiscountProducts = list
        _viewState.value = update
    }

    fun cancelActiveJob()
    {
        handlePendingData()
        homeRepository.cancelActiveJob()
    }

    fun handlePendingData()
    {
        setStateEvent(event = None())
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }
}