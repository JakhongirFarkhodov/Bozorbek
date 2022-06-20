package com.example.bozorbek_vol2.model.main.search

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "search_product_table")
data class SearchProduct(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    val name:String,

    @ColumnInfo(name = "slug")
    val slug:String,

    @ColumnInfo(name = "absolute_url")
    val absolute_url:String
)
