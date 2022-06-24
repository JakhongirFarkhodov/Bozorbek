package com.example.bozorbek_vol2.model.main.home

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "slider_image_table")
data class HomeSliderImage(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "image")
    val image:String,

    @ColumnInfo(name = "text")
    val text:String


)
