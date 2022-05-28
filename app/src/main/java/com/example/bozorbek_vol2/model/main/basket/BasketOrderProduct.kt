package com.example.bozorbek_vol2.model.main.basket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "basket_order_product_table")
data class BasketOrderProduct(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id:Int,

    @ColumnInfo(name = "name")
    val name:String? = null,

    @ColumnInfo(name = "product_name")
    val product_name:String? = null,

    @ColumnInfo(name = "form")
    val from:String? = null,

    @ColumnInfo(name = "color")
    val color:String? = null,

    @ColumnInfo(name = "aroma")
    val aroma:String? = null,

    @ColumnInfo(name = "taste")
    val taste:String? = null,

    @ColumnInfo(name = "organic")
    val organic:Boolean,

    @ColumnInfo(name = "origin")
    val origin:String? = null,

    @ColumnInfo(name = "piece_size")
    val piece_size:String? = null,

    @ColumnInfo(name = "in_piece")
    val in_piece:Boolean,

    @ColumnInfo(name = "price_in_piece")
    val price_in_piece:Int,

    @ColumnInfo(name = "discount_in_piece")
    val discount_in_piece:Int,

    @ColumnInfo(name = "in_gramme")
    val in_gramme:Boolean,

    @ColumnInfo(name = "price_in_gramme")
    val price_in_gramme:Float,

    @ColumnInfo(name = "sum_price_gramme")
    val sum_price_gramme:Float,

    @ColumnInfo(name = "discount_in_gramme")
    val discount_in_gramme:Int,

    @ColumnInfo(name = "size_gramme")
    val size_gramme:Float,

    @ColumnInfo(name = "sum_of_size")
    val sum_of_size:Float,

    @ColumnInfo(name = "size_diameter")
    val size_diameter:Int,

    @ColumnInfo(name = "expiration")
    val expiration:String? = null,

    @ColumnInfo(name = "certification")
    val certification:String? = null,

    @ColumnInfo(name = "condition")
    val condition:String? = null,

    @ColumnInfo(name = "storage_temp")
    val storage_temp:String? = null,

    @ColumnInfo(name = "description")
    val description:String? = null,

    @ColumnInfo(name = "main_image")
    val main_image:String? = null
)
