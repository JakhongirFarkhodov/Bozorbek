package com.example.bozorbek_vol2.network.main.network_services.profile.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileComplaintsRequest(

    @SerializedName("title")
    @Expose
    val title:String,

    @SerializedName("text")
    @Expose
    val text:String

)
