package com.example.bozorbek_vol2.network.main.network_services.basket.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ApproveOrderRequest(

    @SerializedName("address_id")
    @Expose
    val address_id:String

)
