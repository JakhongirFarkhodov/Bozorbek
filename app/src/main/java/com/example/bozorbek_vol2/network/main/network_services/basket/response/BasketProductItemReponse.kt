package com.example.bozorbek_vol2.network.main.network_services.basket.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class BasketProductItemReponse(
    @SerializedName("id")
    @Expose
    val id:Int,

    @SerializedName("name")
    @Expose
    val name:String,

    @SerializedName("form")
    @Expose
    val form:String,

    @SerializedName("color")
    @Expose
    val color:String,

    @SerializedName("aroma")
    @Expose
    val aroma:String,

    @SerializedName("taste")
    @Expose
    val taste:String,

    @SerializedName("organic")
    @Expose
    val organic:Boolean,

    @SerializedName("origin")
    @Expose
    val origin:String,

    @SerializedName("piece_size")
    @Expose
    val piece_size:String,

    @SerializedName("in_piece")
    @Expose
    val in_piece:Boolean,

    @SerializedName("price_in_piece")
    @Expose
    val price_in_piece:Int,

    @SerializedName("discount_in_piece")
    @Expose
    val discount_in_piece:Float,

    @SerializedName("in_gramme")
    @Expose
    val in_gramme:Boolean,

    @SerializedName("price_in_gramme")
    @Expose
    val price_in_gramme:Float,

    @SerializedName("discount_in_gramme")
    @Expose
    val discount_in_gramme:Float,

    @SerializedName("size_gramme")
    @Expose
    val size_gramme:Float,

    @SerializedName("size_diameter")
    @Expose
    val size_diameter:Int,

    @SerializedName("expiration")
    @Expose
    val expiration:String,

    @SerializedName("certification")
    @Expose
    val certification:String,

    @SerializedName("condition")
    @Expose
    val condition:String,

    @SerializedName("storage_temp")
    @Expose
    val storage_temp:String,

    @SerializedName("description")
    @Expose
    val description:String,

    @SerializedName("main_image")
    @Expose
    val main_image:String,

    @SerializedName("product_name")
    @Expose
    val product_name:String
)
