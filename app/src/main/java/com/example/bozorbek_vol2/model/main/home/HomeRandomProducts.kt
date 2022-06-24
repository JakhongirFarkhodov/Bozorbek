package com.example.bozorbek_vol2.model.main.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "home_random_products")
data class HomeRandomProducts(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name ="get_absolute_url")
    val get_absolute_url:String,

    @ColumnInfo(name ="slug")
    val slug:String,

    @ColumnInfo(name ="category")
    val category:String,

    @ColumnInfo(name ="image")
    val image:String,

    @ColumnInfo(name ="price")
    val price:Int,

    @ColumnInfo(name ="discount")
    val discount:Float,

    @ColumnInfo(name ="unit")
    val unit:String

)
