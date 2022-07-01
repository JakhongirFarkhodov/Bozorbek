package com.example.bozorbek_vol2.network.main.network_services.search.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SearchHistoryResponse(

    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("query")
    @Expose
    val query:String,

    @SerializedName("user")
    @Expose
    val user:Int,

    @SerializedName("date_added")
    @Expose
    val date_added:String

)
