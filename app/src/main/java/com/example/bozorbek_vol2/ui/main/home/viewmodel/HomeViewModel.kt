package com.example.bozorbek_vol2.ui.main.home.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
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
import javax.inject.Inject

class HomeViewModel
    @Inject
    constructor(val sessionManager: SessionManager, val homeRepository: HomeRepository)
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