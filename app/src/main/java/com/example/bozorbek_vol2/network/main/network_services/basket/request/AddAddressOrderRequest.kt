package com.example.bozorbek_vol2.network.main.network_services.basket.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class AddAddressOrderRequest(

    @SerializedName("full_address")
    @Expose
    val full_address:String,

    @SerializedName("latitude")
    @Expose
    val latitude:String,

    @SerializedName("longtitude")
    @Expose
    val longtitude:String

)
