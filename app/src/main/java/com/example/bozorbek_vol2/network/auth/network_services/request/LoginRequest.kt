package com.example.bozorbek_vol2.network.auth.network_services.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class LoginRequest(

    @SerializedName("username")
    @Expose
    val phone_number:String,

    @SerializedName("password")
    @Expose
    val password:String

)
