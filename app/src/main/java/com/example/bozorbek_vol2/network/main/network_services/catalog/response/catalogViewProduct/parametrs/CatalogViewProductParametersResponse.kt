package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.parametrs

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogViewProductParametersResponse(

    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("values")
    @Expose
    val values:List<CatalogViewProductParametersValueResponse>

)
