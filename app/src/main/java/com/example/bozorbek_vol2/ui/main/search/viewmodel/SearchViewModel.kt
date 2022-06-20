package com.example.bozorbek_vol2.ui.main.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.search.SearchProduct
import com.example.bozorbek_vol2.repository.main.search.SearchRepository
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.search.state.CheckPreviousAuthUser
import com.example.bozorbek_vol2.ui.main.search.state.SearchStateEvent
import com.example.bozorbek_vol2.ui.main.search.state.SearchViewState
import javax.inject.Inject

class SearchViewModel
    @Inject
    constructor(val searchRepository: SearchRepository, val sessionManager: SessionManager)
    : BaseViewModel<SearchStateEvent, SearchViewState>() {
    override fun initNewViewState(): SearchViewState {
        return SearchViewState()
    }

    override fun handleStateEvent(stateEvent: SearchStateEvent): LiveData<DataState<SearchViewState>> {
        when(stateEvent)
        {
            is SearchStateEvent.CheckPreviousAuthUser ->{
                Log.d(TAG, "handleStateEvent: Запрошиваем у searchViewModel... ")
                return searchRepository.checkPreviousAuthUser()
            }

            is SearchStateEvent.SearchProductEvent ->{
                return searchRepository.searchProduct(stateEvent.query)
            }

            is SearchStateEvent.None ->{
                return return object : LiveData<DataState<SearchViewState>>()
                {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null, null)
                    }
                }
            }
        }
    }

    fun setTokens(checkPreviousAuthUser: CheckPreviousAuthUser)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.checkPreviousAuthUser == checkPreviousAuthUser)
        {
            return
        }
        update.checkPreviousAuthUser = checkPreviousAuthUser
        _viewState.value = update
    }

    fun setSearchProductList(list: List<SearchProduct>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.searchProductList = list
        _viewState.value = update
    }

    fun handlingPendingData()
    {
        setStateEvent(event = SearchStateEvent.None())
    }

    fun cancelActiveJob()
    {
        handlingPendingData()
        searchRepository.cancelActiveJob()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }
}