package com.example.bozorbek_vol2.network.main.network_services.search.response

import com.google.gson.annotations.SerializedName

data class SearchProductResponse(

    @SerializedName("results")
    val results:List<SearchProductResultItemResponse>

)
