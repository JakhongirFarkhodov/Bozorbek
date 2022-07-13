package com.example.bozorbek_vol2.model.main.catalog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "catalog_table")
data class Catalog(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "get_absolute_url")
    val get_absolute_url:String,

    @ColumnInfo(name = "get_image")
    val get_image:String,

    @ColumnInfo(name = "slug")
    val slug:String
):Serializable
