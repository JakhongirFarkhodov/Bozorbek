package com.example.bozorbek_vol2.network.auth.network_services.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetPasswordResponse(
    @SerializedName("username")
    @Expose
    val username:String
)
