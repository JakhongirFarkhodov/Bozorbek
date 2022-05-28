package com.example.bozorbek_vol2.network.main.network_services.basket.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BasketOrderResponse(
    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("address")
    @Expose
    val address:String,

    @SerializedName("created_at")
    @Expose
    val created_at:String,

    @SerializedName("user_approved")
    @Expose
    val user_approved:Boolean,

    @SerializedName("courier")
    @Expose
    val courier:String,

    @SerializedName("total_cost")
    @Expose
    val total_cost:String,

    @SerializedName("delivery_cost")
    @Expose
    val delivery_cost:Int,

    @SerializedName("collect_time")
    @Expose
    val collect_time:String,

    @SerializedName("delivery_time")
    @Expose
    val delivery_time:String,

    @SerializedName("items")
    @Expose
    val items:List<BasketOrderItemResponse>
)
