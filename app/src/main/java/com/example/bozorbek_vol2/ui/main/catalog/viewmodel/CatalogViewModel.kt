package com.example.bozorbek_vol2.ui.main.catalog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.repository.main.catalog.CatalogRepository
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogStateEvent.*
import com.example.bozorbek_vol2.ui.main.catalog.state.CatalogViewState
import com.example.bozorbek_vol2.util.AbsentLiveData
import javax.inject.Inject

class CatalogViewModel
@Inject
constructor(val catalogRepository: CatalogRepository, val sessionManager: SessionManager)
    : BaseViewModel<CatalogStateEvent, CatalogViewState>() {
    override fun initNewViewState(): CatalogViewState {
        return CatalogViewState()
    }

    override fun handleStateEvent(stateEvent: CatalogStateEvent): LiveData<DataState<CatalogViewState>> {
        when (stateEvent) {
            is GetCatalogListOfData -> {
                Log.d(TAG, "handleStateEvent: Запрос на viewModel")
                return catalogRepository.getCatalogList()
            }

            is GetCatalogProductListOfData ->{
                return catalogRepository.getCatalogProduct(slug = stateEvent.slug)
            }

            is GetCatalogViewProductListOfData ->{
                return catalogRepository.getCatalogParameters(category_slug = stateEvent.category_slug, product_slug = stateEvent.product_slug)
            }

            is GetCatalogViewProductBySortValue -> {
                return catalogRepository.getCatalogViewProductBySortValue(sort_value = stateEvent.sort_value)
            }

            is AddCatalogOrderItem ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    Log.d(TAG, "handleStateEvent: AddCatalogOrderItem")
                    catalogRepository.addOrderItem(authToken = authToken, product_item_id = stateEvent.product_item_id, quantity = stateEvent.quantity, unit = stateEvent.unit)
                }?:AbsentLiveData.create()
            }

            is None -> {
                return AbsentLiveData.create()
            }
        }
    }

    fun setCatalogList(list: List<Catalog>) {
        val update = getCurrentViewStateOrCreateNew()
        update.catalogList.list = list
        _viewState.value = update
    }

    fun setCatalogProductListOfData(list: List<CatalogProduct>)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.catalogProductList.list = list
        _viewState.value = update
    }

    fun setParametersValue(parametersValue: ParametersValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        update.parametersValue = parametersValue
        _viewState.value = update
    }

    fun setAddOrderItemMessage(message:String)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.message == message)
        {
            return
        }
        update.message = message
        _viewState.value = update
    }



}