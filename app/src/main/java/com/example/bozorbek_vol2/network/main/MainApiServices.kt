package com.example.bozorbek_vol2.network.main

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.network.main.network_services.basket.request.AddAddressOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.request.ApproveOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.response.AddAddressOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.ApproveOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.BasketOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.basket.response.GetBasketListAddressResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.request.CatalogAddItemOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogAddOrderItemResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogProduct.CatalogProductsListResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalog.CatalogResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogViewProductListResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ProfileResponse
import com.example.bozorbek_vol2.util.GenericApiResponse
import retrofit2.http.*

interface MainApiServices {

    @GET("/customer/get_info/")
    fun getProfileInfo(@Header("Authorization") token: String): LiveData<GenericApiResponse<ProfileResponse>>

    //Catalog
    @GET("/products/categories/")
    fun getCatalogList(): LiveData<GenericApiResponse<List<CatalogResponse>>>

    @GET("/products/{slug}/")
    fun getCatalogProductList(@Path("slug") slug: String): LiveData<GenericApiResponse<CatalogProductsListResponse>>

    @GET("/products/{category_slug}/{product_slug}/")
    fun getCatalogViewProductList(
        @Path("category_slug") category_slug: String,
        @Path("product_slug") product_slug: String
    ): LiveData<GenericApiResponse<CatalogViewProductListResponse>>

    @POST("/orders/add_item/")
    fun addOrderItem(
        @Header("Authorization") token: String,
        @Body catalogAddItemOrder: CatalogAddItemOrderRequest
    ): LiveData<GenericApiResponse<CatalogAddOrderItemResponse>>

    //Basket
    @GET("/orders/cart/")
    fun getBasketOrderList(@Header("Authorization") accessToken: String): LiveData<GenericApiResponse<BasketOrderResponse>>

    @POST("/orders/add_address/")
    fun setOrderAddress(@Header("Authorization") accessToken: String, @Body addAddressOrderRequest: AddAddressOrderRequest):LiveData<GenericApiResponse<AddAddressOrderResponse>>

    @GET("/orders/address_list/")
    fun getBasketAddressList(@Header("Authorization") accessToken: String):LiveData<GenericApiResponse<List<GetBasketListAddressResponse>>>

    @POST("/orders/approve_order/")
    fun approveOrder(@Header("Authorization") accessToken: String, @Body
    approveOrderRequest: ApproveOrderRequest):LiveData<GenericApiResponse<ApproveOrderResponse>>
}