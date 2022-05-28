package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.items.features

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogViewProductsItemsFeaturesResponse(

    @SerializedName("value_id")
    @Expose
    val value_id:Int,

    @SerializedName("value")
    @Expose
    val value:String,

    @SerializedName("parameter")
    @Expose
    val parameter:String,

    @SerializedName("parameter_id")
    @Expose
    val parameter_id:Int

)
