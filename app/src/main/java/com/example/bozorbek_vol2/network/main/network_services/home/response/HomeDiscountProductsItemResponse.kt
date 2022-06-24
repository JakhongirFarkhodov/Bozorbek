package com.example.bozorbek_vol2.network.main.network_services.home.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class HomeDiscountProductsItemResponse(

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("get_absolute_url")
    @Expose
    val get_absolute_url:String,

    @SerializedName("slug")
    @Expose
    val slug:String,

    @SerializedName("category")
    @Expose
    val category:String,

    @SerializedName("image")
    @Expose
    val image:String,

    @SerializedName("price")
    @Expose
    val price:Int,

    @SerializedName("discount")
    @Expose
    val discount:Float,

    @SerializedName("unit")
    @Expose
    val unit:String

)
