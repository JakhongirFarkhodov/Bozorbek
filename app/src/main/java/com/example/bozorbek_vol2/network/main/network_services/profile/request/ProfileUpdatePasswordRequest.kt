package com.example.bozorbek_vol2.network.main.network_services.profile.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileUpdatePasswordRequest(

    @SerializedName("password")
    @Expose
    val password:String,

    @SerializedName("new_password")
    @Expose
    val new_password:String

)
