package com.example.bozorbek_vol2.network.auth.network_services.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @SerializedName("access")
    @Expose
    val access_token:String,

    @SerializedName("refresh")
    @Expose
    val refresh_token:String

)
