package com.example.bozorbek_vol2.network.main.network_services.basket.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BasketOrderItemResponse(
    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("price")
    @Expose
    val price:String,

    @SerializedName("unit")
    @Expose
    val unit:String,

    @SerializedName("quantity")
    @Expose
    val quantity:Int,

    @SerializedName("product_item")
    @Expose
    val product_item:BasketProductItemReponse
)
