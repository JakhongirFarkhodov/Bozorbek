package com.example.bozorbek_vol2.network.main.network_services.profile.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileReadyPackageAutoOrder(

    @SerializedName("package")
    @Expose
    val package_id:Int,

    @SerializedName("week_frequency")
    @Expose
    val week_frequency:Int,

    @SerializedName("week_day")
    @Expose
    val week_day:Int,


//    @SerializedName("address")
//    @Expose
//    val address:Int,
//
//    @SerializedName("client_name")
//    @Expose
//    val client_name:String,
//
//    @SerializedName("client_phone")
//    @Expose
//    val client_phone:String,
//
//
//    @SerializedName("author")
//    @Expose
//    val author:String

)
