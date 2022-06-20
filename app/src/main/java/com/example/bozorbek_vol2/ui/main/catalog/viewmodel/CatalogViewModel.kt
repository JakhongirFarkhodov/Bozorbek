package com.example.bozorbek_vol2.ui.main.catalog.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
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
                return catalogRepository.getCatalogViewProduct(category_slug = stateEvent.category_slug, product_slug = stateEvent.product_slug)
            }

            is GetCatalogViewProductBySortValue -> {
                Log.d(TAG, "GetCatalogViewProductBySortValue:${stateEvent.sort_value} ")
                return catalogRepository.getSelectedCatalogViewProduct(sortValue = stateEvent.sort_value)
            }

            is GetCatalogViewProductBySortAndProductOwnerValue ->{
                return catalogRepository.getCatalogViewProductBySortAndProductOwnerValue(stateEvent.sort_value, stateEvent.productOwner_value)
            }

            is GetCatalogViewProductBySortAndProductOwnerAndPaketValue ->{
                return catalogRepository.getCatalogViewProductBySortAndProductOwnerAndPaketValue(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value)
            }

            is GetCatalogViewProductByGramme ->{
                return catalogRepository.getCatalogViewProductByGramme(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme)
            }

            is GetCatalogViewProductByPiece ->{
                return catalogRepository.getCatalogViewProductByPiece(stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.piece)
            }

            is GetCatalogViewProductBySizeLarge ->{
                return catalogRepository.getCatalogViewProductBySizeLarge(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.large)
            }
            is GetCatalogViewProductBySizeMiddle ->{
                return catalogRepository.getCatalogViewProductBySizeMiddle(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.middle)
            }
            is GetCatalogViewProductBySizeSmall ->{
                return catalogRepository.getCatalogViewProductBySizeSmall(
                    stateEvent.sort_value, stateEvent.productOwner_value, stateEvent.paket_value, stateEvent.gramme, stateEvent.piece, stateEvent.small)
            }
            is AddCatalogOrderItem ->{
                return sessionManager.cachedAuthToken.value?.let { authToken ->
                    catalogRepository.addItemCatalogViewProduct(
                        authToken = authToken,
                        product_item_id = stateEvent.product_item_id,
                        quantity = stateEvent.quantity,
                        unit = stateEvent.unit,
                        size = stateEvent.size,
                        sortValue = stateEvent.sortValue)
                }?:AbsentLiveData.create()
            }

            is None -> {
                return return object : LiveData<DataState<CatalogViewState>>()
                {
                    override fun onActive() {
                        super.onActive()
                        value = DataState.data(null, null)
                    }
                }
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

    fun handlePendingData()
    {
        setStateEvent(event = CatalogStateEvent.None())
    }

    fun cancelActiveJob()
    {
        handlePendingData()
        catalogRepository.cancelActiveJob()
    }

    override fun onCleared() {
        super.onCleared()
        cancelActiveJob()
    }

}