package com.example.bozorbek_vol2.model.main.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "profile_ready_packages")
data class ProfileReadyPackages(

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
    @Expose
    val category_name:String,

    @ColumnInfo(name = "get_absolute_url")
    @Expose
    val get_absolute_url:String,

    @ColumnInfo(name = "get_image")
    @Expose
    val get_image:String,

    @ColumnInfo(name = "slug")
    @Expose
    val slug:String


)
