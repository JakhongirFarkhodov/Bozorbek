package com.example.bozorbek_vol2.ui

interface OnDataStateChangeListener {

    fun onDataStateChange(dataState: DataState<*>)
    fun expendAppBar()
    fun getOnOrderItemCount(itemCount:Int)
    fun getItemCount():Int
    fun isStoragePermissionGranted():Boolean

}