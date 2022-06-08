package com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileListOfAllReadyPackagesResponse(

    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("author")
    @Expose
    val author:Int,

    @SerializedName("visibility")
    @Expose
    val visibility:Boolean,

    @SerializedName("total_cost")
    @Expose
    val total_cost:Int,

    @SerializedName("items_count")
    @Expose
    val items_count:Int,

    @SerializedName("categories")
    @Expose
    val categories:List<ProfileListOfAllReadyPackagesCategoryResponse>

)
