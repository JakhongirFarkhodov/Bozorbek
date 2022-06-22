package com.example.bozorbek_vol2.network.main.network_services.basket.request.save_package

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SaveReadyPackageRequest(

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("author")
    @Expose
    val author:String,

    @SerializedName("visibility")
    @Expose
    val visibility:Boolean,

    @SerializedName("items")
    @Expose
    val items:List<SaveReadyPackageItemRequest>

)
