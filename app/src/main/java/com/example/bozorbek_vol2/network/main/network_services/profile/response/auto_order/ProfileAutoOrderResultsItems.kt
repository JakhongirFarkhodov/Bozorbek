package com.example.bozorbek_vol2.network.main.network_services.profile.response.auto_order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileAutoOrderResultsItems(
    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("address")
    @Expose
    val address:String?,

    @SerializedName("client_name")
    @Expose
    val client_name:String?,

    @SerializedName("client_phone")
    @Expose
    val client_phone:String?,

    @SerializedName("week_frequency")
    @Expose
    val week_frequency:Int,

    @SerializedName("week_day")
    @Expose
    val week_day:Int,

    @SerializedName("created_date")
    @Expose
    val created_date:String,

    @SerializedName("author")
    @Expose
    val author:Int,

    @SerializedName("package")
    @Expose
    val package_item:ProfileAutoOrderPackageItemResponse
)
