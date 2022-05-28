package com.example.bozorbek_vol2.network.main.network_services.catalog.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogAddItemOrderRequest(

    @SerializedName("product_item_id")
    @Expose
    val product_item_id:String,

    @SerializedName("quantity")
    @Expose
    val quantity:Int,

    @SerializedName("unit")
    @Expose
    val unit:String

)
