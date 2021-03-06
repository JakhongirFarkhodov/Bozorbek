package com.example.bozorbek_vol2.model.main.catalog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_view_product_table")
data class CatalogViewProduct(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id:Int,

    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "form")
    val form:String,

    @ColumnInfo(name = "color")
    val color:String,

    @ColumnInfo(name = "aroma")
    val aroma:String,

    @ColumnInfo(name = "taste")
    val taste:String,

    @ColumnInfo(name = "organic")
    val organic:Boolean,

    @ColumnInfo(name = "origin")
    val origin:String,

    @ColumnInfo(name = "piece_size")
    val piece_size:String,

    @ColumnInfo(name = "in_piece")
    val in_piece:Boolean,

    @ColumnInfo(name = "price_in_piece")
    val price_in_piece:Float,

    @ColumnInfo(name = "discount_in_piece")
    val discount_in_piece:Float,

    @ColumnInfo(name = "in_gramme")
    val in_gramme:Boolean,

    @ColumnInfo(name = "price_in_gramme")
    val price_in_gramme:Float,

    @ColumnInfo(name = "discount_in_gramme")
    val discount_in_gramme:Float,

    @ColumnInfo(name = "size_gramme")
    val size_gramme:Int,

    @ColumnInfo(name = "size_diameter")
    val size_diameter:Int,

    @ColumnInfo(name = "expiration")
    val expiration:String,

    @ColumnInfo(name = "certification")
    val certification:String,

    @ColumnInfo(name = "condition")
    val condition:String,

    @ColumnInfo(name = "storage_temp")
    val storage_temp:String,

    @ColumnInfo(name = "description")
    val description:String,

    @ColumnInfo(name = "main_image")
    val main_image:String,

    @ColumnInfo(name = "product_name")
    val product_name:String,

    @ColumnInfo(name = "large")
    val large:Boolean,

    @ColumnInfo(name = "large_percent")
    val large_percent:Float,

    @ColumnInfo(name = "middle")
    val middle:Boolean,

    @ColumnInfo(name = "middle_percent")
    val middle_percent:Float,

    @ColumnInfo(name = "small")
    val small:Boolean,

    @ColumnInfo(name = "small_percent")
    val small_percent:Float,

    //Sort
    @ColumnInfo(name = "sort_value_id")
    val sort_value_id:Int,

    @ColumnInfo(name = "sort_value")
    val sort_value:String,

    @ColumnInfo(name = "sort_parameter")
    val sort_parameter:String,

    @ColumnInfo(name = "sort_parameter_id")
    val sort_parameter_id:Int,

    //ProductOwner
    @ColumnInfo(name = "product_owner_value_id")
    val product_owner_value_id:Int,

    @ColumnInfo(name = "product_owner_value")
    val product_owner_value:String,

    @ColumnInfo(name = "product_owner_parameter")
    val product_owner_parameter:String,

    @ColumnInfo(name = "product_owner_parameter_id")
    val product_owner_parameter_id:Int,

    //Paket
    @ColumnInfo(name = "paket_value_id")
    val paket_value_id:Int,

    @ColumnInfo(name = "paket_value")
    val paket_value:String,

    @ColumnInfo(name = "paket_parameter")
    val paket_parameter:String,

    @ColumnInfo(name = "paket_parameter_id")
    val paket_parameter_id:Int
)
