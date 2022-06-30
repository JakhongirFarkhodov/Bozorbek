package com.example.bozorbek_vol2.network.main.network_services.profile.response.auto_order

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileAutoOrderResponse(
    @SerializedName("count")
    @Expose
    val count:String,

    @SerializedName("next")
    @Expose
    val next:String,

    @SerializedName("previous")
    @Expose
    val previous:String,

    @SerializedName("results")
    @Expose
    val results:List<ProfileAutoOrderResultsItems>
)
