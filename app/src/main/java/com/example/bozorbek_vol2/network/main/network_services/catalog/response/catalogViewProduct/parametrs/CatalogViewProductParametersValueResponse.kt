package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.parametrs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogViewProductParametersValueResponse(
    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("value")
    @Expose
    val value:String
)
