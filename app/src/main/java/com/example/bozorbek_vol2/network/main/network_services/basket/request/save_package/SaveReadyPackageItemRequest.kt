package com.example.bozorbek_vol2.network.main.network_services.basket.request.save_package

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaveReadyPackageItemRequest(

    @SerializedName("product_item")
    @Expose
    val product_item:Int,

    @SerializedName("unit")
    @Expose
    val unit:String,


)
