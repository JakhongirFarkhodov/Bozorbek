package com.example.bozorbek_vol2.network.main.network_services.home.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeSliderImagesResponse(

    @SerializedName("count")
    @Expose
    val count:Int,

    @SerializedName("next")
    @Expose
    val next:String,

    @SerializedName("previous")
    @Expose
    val previous:String,

    @SerializedName("results")
    @Expose
    val results:List<HomeSliderImageItemsResponse>

)
