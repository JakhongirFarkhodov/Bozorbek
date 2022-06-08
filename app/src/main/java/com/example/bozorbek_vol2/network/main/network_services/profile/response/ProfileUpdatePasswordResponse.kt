package com.example.bozorbek_vol2.network.main.network_services.profile.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileUpdatePasswordResponse(

    @SerializedName("message")
    @Expose
    val message:String

)
