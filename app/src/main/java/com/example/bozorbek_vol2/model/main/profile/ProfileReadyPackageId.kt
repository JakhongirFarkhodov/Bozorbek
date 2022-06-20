package com.example.bozorbek_vol2.model.main.profile

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "profile_ready_package_id")
data class ProfileReadyPackageId(

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
    val size_gramme:Float,

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

    @ColumnInfo(name = "quantity")
    val quantity:Int,

    @ColumnInfo(name = "unit")
    val unit:String,

    @ColumnInfo(name = "size")
    val size:String,

    @ColumnInfo(name = "price")
    val price:Float
):Parcelable




