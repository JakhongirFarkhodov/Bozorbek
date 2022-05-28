package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogAddOrderItemResponse(
    @SerializedName("message")
    @Expose
    val message:String
)
