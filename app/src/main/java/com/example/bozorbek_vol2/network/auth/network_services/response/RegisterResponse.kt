package com.example.bozorbek_vol2.network.auth.network_services.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterResponse(
    @SerializedName("phone_num")
    @Expose
    val phone_num:String,

    @SerializedName("message")
    @Expose
    val message:String
)
