package com.example.bozorbek_vol2.network.auth.network_services.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class RegisterRequest(

    @SerializedName("phone_num")
    @Expose
    val phone_num:String

)
