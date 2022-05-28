package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct

import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.parametrs.CatalogViewProductParametersResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.items.CatalogViewProductResponse
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class CatalogViewProductListResponse(

    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("get_absolute_url")
    @Expose
    val get_absolute_url:String,

    @SerializedName("slug")
    @Expose
    val slug:String,

    @SerializedName("category")
    @Expose
    val category:String,

    @SerializedName("parameters")
    @Expose
    val parameters:List<CatalogViewProductParametersResponse>,

    @SerializedName("items")
    @Expose
    val items:List<CatalogViewProductResponse>
)
