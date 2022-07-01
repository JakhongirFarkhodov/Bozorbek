package com.example.bozorbek_vol2.network.main

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.network.main.network_services.basket.request.AddAddressOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.request.ApproveOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.request.BasketRemoveProductRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.request.save_package.SaveReadyPackageRequest
import com.example.bozorbek_vol2.network.main.network_services.basket.response.*
import com.example.bozorbek_vol2.network.main.network_services.catalog.request.CatalogAddItemOrderRequest
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalog.CatalogResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogProduct.CatalogProductsListResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogAddOrderItemResponse
import com.example.bozorbek_vol2.network.main.network_services.catalog.response.catalogViewProduct.CatalogViewProductListResponse
import com.example.bozorbek_vol2.network.main.network_services.home.response.HomeDiscountProductsResponse
import com.example.bozorbek_vol2.network.main.network_services.home.response.HomeRandomProductsResponse
import com.example.bozorbek_vol2.network.main.network_services.home.response.HomeSliderImagesResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileComplaintsRequest
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileReadyPackageAutoOrder
import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileUpdatePasswordRequest
import com.example.bozorbek_vol2.network.main.network_services.profile.response.*
import com.example.bozorbek_vol2.network.main.network_services.profile.response.active_order.ProfileActiveOrHistoryOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.auto_order.ProfileAutoOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_package_id.ReadyPackageIdResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages.ProfileAllReadyPackagesAddItemToBasketResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages.ProfileAllReadyPackagesResponse
import com.example.bozorbek_vol2.network.main.network_services.profile.response.ready_packages.ProfileSetReadyPackageAutoOrderResponse
import com.example.bozorbek_vol2.network.main.network_services.search.response.SearchHistoryResponse
import com.example.bozorbek_vol2.network.main.network_services.search.response.SearchProductResponse
import com.example.bozorbek_vol2.util.GenericApiResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface MainApiServices {

    //Home
    @GET("/sliders/")
    fun getSliderImages():LiveData<GenericApiResponse<HomeSliderImagesResponse>>

    @GET("/products/random_products/")
    fun getRandomProducts():LiveData<GenericApiResponse<List<HomeRandomProductsResponse>>>

    @GET("/products/discount_products/")
    fun getDiscountProducts():LiveData<GenericApiResponse<HomeDiscountProductsResponse>>

    //Profile
    @GET("/customer/get_info/")
    fun getProfileInfo(@Header("Authorization") token: String): LiveData<GenericApiResponse<ProfileResponse>>

    @Multipart
    @POST("/customer/upload_image/")
    fun uploadUserImage(@Header("Authorization") token: String, @Part image: MultipartBody.Part?):LiveData<GenericApiResponse<ProfileUploadImageResponse>>

    //Profile ready package
    @GET("/readypackages/")
    fun getAllReadyPackages(@Header("Authorization") token: String, @Query("type") type:String):LiveData<GenericApiResponse<ProfileAllReadyPackagesResponse>>

    @POST("/readypackages/")
    fun setCreatedReadyPackage(@Header("Authorization") token: String, @Body saveReadyPackageRequest: SaveReadyPackageRequest):LiveData<GenericApiResponse<SaveReadyPackageResponse>>

    @GET("/readypackages/{id}")
    fun getReadyPackageById(@Header("Authorization") token: String, @Path("id") ready_package_id:Int):LiveData<GenericApiResponse<ReadyPackageIdResponse>>

    @POST("/readypackages/add_to_cart/{id}/")
    fun addItemReadyPackageToBasket(@Header("Authorization") token: String, @Path("id") ready_package_id:String):LiveData<GenericApiResponse<ProfileAllReadyPackagesAddItemToBasketResponse>>

    @HTTP(method = "DELETE", path = "/readypackages/{id}/", hasBody = false)
    fun deleteReadyPackageById(@Header("Authorization") token: String, @Path("id") ready_package_id:Int):LiveData<GenericApiResponse<Void>>

    @POST("/orders/auto-order/")
    fun setReadyPackageAutoOrder(@Header("Authorization") token: String, @Body profileReadyPackageAutoOrder: ProfileReadyPackageAutoOrder):LiveData<GenericApiResponse<ProfileSetReadyPackageAutoOrderResponse>>

    @GET("/orders/auto-order/")
    fun getAutoOrderItem(@Header("Authorization") token: String):LiveData<GenericApiResponse<ProfileAutoOrderResponse>>

    //Notification
    @GET("/notifications/")
    fun getNotifications(@Header("Authorization") token: String):LiveData<GenericApiResponse<List<ProfileNotificationResponse>>>

    //Update password
    @POST("/customer/update_password/")
    fun updateProfilePassword(@Header("Authorization") token: String, @Body profileUpdatePasswordRequest: ProfileUpdatePasswordRequest):LiveData<GenericApiResponse<ProfileUpdatePasswordResponse>>

    //Complaints
    @POST("/reviews/post_review/")
    fun setComplaint(@Header("Authorization") token: String, @Body profileComplaints: ProfileComplaintsRequest) : LiveData<GenericApiResponse<ProfileComplaintsResponse>>

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

    @GET("/orders/order_list/")
    fun getActiveOrHistoryOrder(@Header("Authorization") accessToken: String):LiveData<GenericApiResponse<List<ProfileActiveOrHistoryOrderResponse>>>

    @POST("/orders/remove_item/")
    fun removeBasketOrderProductById(@Header("Authorization") accessToken: String, @Body basketRemoveProductRequest: BasketRemoveProductRequest):LiveData<GenericApiResponse<BasketRemoveProductResponse>>

    @POST("/orders/add_address/")
    fun setOrderAddress(@Header("Authorization") accessToken: String, @Body addAddressOrderRequest: AddAddressOrderRequest):LiveData<GenericApiResponse<AddAddressOrderResponse>>

    @GET("/orders/address_list/")
    fun getBasketAddressList(@Header("Authorization") accessToken: String):LiveData<GenericApiResponse<List<GetBasketListAddressResponse>>>

    @POST("/orders/approve_order/")
    fun approveOrder(@Header("Authorization") accessToken: String, @Body
    approveOrderRequest: ApproveOrderRequest):LiveData<GenericApiResponse<ApproveOrderResponse>>

    //Search
    @GET("/search/product/{query}")
    fun searchProduct(@Header("Authorization") accessToken: String, @Path("query") query:String):LiveData<GenericApiResponse<SearchProductResponse>>

    @GET("/search/history/")
    fun getSearchHistory(@Header("Authorization") accessToken: String):LiveData<GenericApiResponse<List<SearchHistoryResponse>>>
}