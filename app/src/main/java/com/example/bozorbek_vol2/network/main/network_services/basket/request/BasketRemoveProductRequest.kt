package com.example.bozorbek_vol2.network.main.network_services.basket.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BasketRemoveProductRequest(

    @SerializedName("item_id")
    @Expose
    val remove_item_id:String

)
