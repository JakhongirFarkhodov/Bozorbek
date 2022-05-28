package com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogProduct

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


data class CatalogProductsListResponse (
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("get_absolute_url")
    @Expose
    var getAbsoluteUrl: String? = null,

    @SerializedName("image")
    @Expose
    var image: String? = null,

    @SerializedName("slug")
    @Expose
    var slug: String? = null,

    @SerializedName("products")
    @Expose
    var products: List<CatalogProductResponse>
)

