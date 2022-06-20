package com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_package_id

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class PackageItemIdResponse(

    @SerializedName("quantity")
    @Expose
    val quantity:Int,

    @SerializedName("unit")
    @Expose
    val unit:String,

    @SerializedName("size")
    @Expose
    val size:String,

    @SerializedName("product_item_id")
    @Expose
    val product_item_id:Int,

    @SerializedName("price")
    @Expose
    val price:Float,

    @SerializedName("product_item")
    @Expose
    val product_item:ProductItemIdResponse

)
