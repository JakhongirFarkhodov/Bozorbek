package com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileSetReadyPackageAutoOrderResponse(
    @SerializedName("id")
    @Expose
    val id:Int
)
