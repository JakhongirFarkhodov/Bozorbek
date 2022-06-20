package com.example.bozorbek_vol2.network.main.network_services.profile.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ProfileNotificationResponse(

    @SerializedName("to_customer")
    @Expose
    val to_customer:Int,

    @SerializedName("title")
    @Expose
    val title:String,

    @SerializedName("content")
    @Expose
    val content:String,

    @SerializedName("is_read")
    @Expose
    val is_read:Boolean,

    @SerializedName("created_at")
    @Expose
    val created_at:String,

    @SerializedName("id")
    @Expose
    val id:Int

)
