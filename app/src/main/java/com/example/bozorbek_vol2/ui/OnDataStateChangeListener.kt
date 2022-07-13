package com.example.bozorbek_vol2.ui

import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileReadyPackageAutoOrder
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.model.CategoryData

interface OnDataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>)
    fun expendAppBar()
    fun getOnOrderItemCount(itemCount:Int)
    fun getItemCount():Int
    fun isStoragePermissionGranted():Boolean

    fun setCatalogListOfObject(list:List<Catalog>)
    fun getCatalogListOfObject():List<Catalog>

    fun setCatalogProductPosition(position:Int)
    fun getCatalogProductPosition():Int


    fun setListOfObjects(list: List<BasketOrderProduct>)
    fun getListOfObjects():List<BasketOrderProduct>

    fun setReadyPackageListOfItems(list: List<ProfileReadyPackageId>)
    fun getReadyPackageListOfItems():List<ProfileReadyPackageId>

    fun setCategoryReadyPackage(list: List<CategoryData>)
    fun getCategoryReadyPackage():List<CategoryData>
    fun setCategoryReadyPackageId(id:Int)
    fun getCategoryReadyPackageId():Int
    fun setProfileReadyPackageAutoOrderParameters(profileReadyPackageAutoOrder: ProfileReadyPackageAutoOrder)
    fun getProfileReadyPackageAutoOrderParameters():ProfileReadyPackageAutoOrder
    fun setTriggerProfileReadyPackageAutoOrderParameters(trigger:Boolean)
    fun getTriggerProfileReadyPackageAutoOrderParameters():Boolean

    fun setSaveButtonClick(click:Boolean)
    fun isSaveButtonClick():Boolean

}