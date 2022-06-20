package com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_package_id

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ReadyPackageIdResponse(

    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("author")
    @Expose
    val author:String,

    @SerializedName("visibility")
    @Expose
    val visibility:Boolean,

    @SerializedName("total_cost")
    @Expose
    val total_cost:String,

    @SerializedName("items")
    @Expose
    val items:List<PackageItemIdResponse>

)
