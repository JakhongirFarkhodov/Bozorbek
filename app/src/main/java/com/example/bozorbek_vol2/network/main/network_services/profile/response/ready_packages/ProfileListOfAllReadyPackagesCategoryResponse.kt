package com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileListOfAllReadyPackagesCategoryResponse(
    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("get_absolute_url")
    @Expose
    val get_absolute_url:String,

    @SerializedName("get_image")
    @Expose
    val get_image:String,

    @SerializedName("slug")
    @Expose
    val slug:String
)
