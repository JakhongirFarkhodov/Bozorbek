package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogProduct

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class CatalogProductResponse (
    @SerializedName("name")
    @Expose
    var name: String,

    @SerializedName("get_absolute_url")
    @Expose
    var getAbsoluteUrl: String,

    @SerializedName("slug")
    @Expose
    var slug: String,

    @SerializedName("category")
    @Expose
    var category: Int,

    @SerializedName("image")
    @Expose
    var image: String,

    @SerializedName("price")
    @Expose
    var price: Float,

    @SerializedName("discount")
    @Expose
    var discount: Float,

    @SerializedName("unit")
    @Expose
    var unit: String
)