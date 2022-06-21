package com.example.bozorbek_vol2.ui.main.search.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.model.main.search.SearchProduct
import com.example.bozorbek_vol2.repository.main.search.SearchRepository
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.search.state.CheckPreviousAuthUser
import com.example.bozorbek_vol2.ui.main.search.state.SearchStateEvent
import com.example.bozorbek_vol2.ui.main.search.state.SearchViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
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

            is SearchStateEvent.GetCatalogViewProductListOfData ->{
                return searchRepository.getCatalogViewProduct(category_slug = stateEvent.category_slug, product_slug = stateEvent.product_slug)
            }

            is SearchStateEvent.GetCatalogViewProductBySortValue -> {
                Log.d(TAG, "GetCatalogViewProductBySortValue:${stateEvent.sort_value} ")
                return searchRepository.getSelectedCatalogViewProduct(sortValue = stateEvent.sort_value)
            }

            is SearchStateEvent.GetCatalogViewProductBySortAndProductOwnerValue ->{
                return searchRepository.getCatalogViewProductBySortAndProductOwnerValue(stateEvent.sort_value, stateEvent.productOwner_value)
            }

            is SearchStateEvent.GetCatalogViewProductBySortAndProductOwnerAndPaketValue ->{
                return searchRepository.getCatalogViewProductBySortAndProductOwnerAndPaketValue(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value)
            }

            is SearchStateEvent.GetCatalogViewProductByGramme ->{
                return searchRepository.getCatalogViewProductByGramme(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme)
            }

            is SearchStateEvent.GetCatalogViewProductByPiece ->{
                return searchRepository.getCatalogViewProductByPiece(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.piece)
            }

            is SearchStateEvent.GetCatalogViewProductBySizeLarge ->{
                return searchRepository.getCatalogViewProductBySizeLarge(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.large)
            }
            is SearchStateEvent.GetCatalogViewProductBySizeMiddle ->{
                return searchRepository.getCatalogViewProductBySizeMiddle(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.middle)
            }
            is SearchStateEvent.GetCatalogViewProductBySizeSmall ->{
                return searchRepository.getCatalogViewProductBySizeSmall(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.small)
            }
            is SearchStateEvent.AddCatalogOrderItem ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    searchRepository.addItemCatalogViewProduct(
                        authToken = authToken,
                        product_item_id = stateEvent.product_item_id,
                        quantity = stateEvent.quantity,
                        unit = stateEvent.unit,
                        size = stateEvent.size,
                        sortValue = stateEvent.sortValue)
                }?: AbsentLiveData.create()
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

    fun setParametersValue(parametersValue: ParametersValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.parametersValue = parametersValue
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