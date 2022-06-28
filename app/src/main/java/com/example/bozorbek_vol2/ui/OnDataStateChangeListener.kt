package com.example.bozorbek_vol2.ui

import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackageId

interface OnDataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>)
    fun expendAppBar()
    fun getOnOrderItemCount(itemCount:Int)
    fun getItemCount():Int
    fun isStoragePermissionGranted():Boolean

    fun setListOfObjects(list: List<BasketOrderProduct>)
    fun getListOfObjects():List<BasketOrderProduct>

    fun setReadyPackageListOfItems(list: List<ProfileReadyPackageId>)
    fun getReadyPackageListOfItems():List<ProfileReadyPackageId>

    fun setSaveButtonClick(click:Boolean)
    fun isSaveButtonClick():Boolean

}