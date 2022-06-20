package com.example.bozorbek_vol2.network.main.network_services.search.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchProductResultItemResponse(

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("slug")
    @Expose
    val slug:String,

    @SerializedName("absolute_url")
    @Expose
    val absolute_url:String

)
