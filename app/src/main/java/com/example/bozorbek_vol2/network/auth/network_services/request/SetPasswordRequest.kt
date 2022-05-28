package com.example.bozorbek_vol2.network.auth.network_services.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SetPasswordRequest(
    @SerializedName("phone_num")
    @Expose
    val phone_num:String,

    @SerializedName("sms_code")
    @Expose
    val sms_code:String,

    @SerializedName("password")
    @Expose
    val password:String,

    @SerializedName("first_name")
    @Expose
    val first_name:String,
)