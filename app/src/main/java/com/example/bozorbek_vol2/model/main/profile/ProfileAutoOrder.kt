package com.example.bozorbek_vol2.model.main.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_auto_order_table")
data class ProfileAutoOrder(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="package_id")
    val package_id:Int,

    @ColumnInfo(name="id")
    val id:Int,

    @ColumnInfo(name = "package_name")
    val package_name:String,

    @ColumnInfo(name = "author")
    val author:Int,

    @ColumnInfo(name = "visibility")
    val visibility:Boolean,

    @ColumnInfo(name = "total_cost")
    val total_cost:Int,

    @ColumnInfo(name = "items_count")
    val items_count:Int,

    @ColumnInfo(name = "category_name")
    val category_name:String,

    @ColumnInfo(name = "get_absolute_url")
    val get_absolute_url:String,

    @ColumnInfo(name = "get_image")
    val get_image:String,

    @ColumnInfo(name = "slug")
    val slug:String,

    @ColumnInfo(name = "week_frequency")
    val week_frequency:Int,

    @ColumnInfo(name = "week_day")
    val week_day:Int,

    @ColumnInfo(name = "created_date")
    val created_date:String


)