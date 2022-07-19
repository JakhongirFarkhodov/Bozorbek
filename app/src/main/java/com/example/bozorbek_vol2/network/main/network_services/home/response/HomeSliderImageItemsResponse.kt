package com.example.bozorbek_vol2.network.main.network_services.home.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeSliderImageItemsResponse(

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("image")
    @Expose
    val image:String,

    @SerializedName("text")
    @Expose
    val text:String? = null

)
