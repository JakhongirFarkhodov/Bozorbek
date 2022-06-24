package com.example.bozorbek_vol2.model.main.catalog

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "catalog_product_table")
data class CatalogProduct(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "get_absolute_url")
    var getAbsoluteUrl: String,

    @ColumnInfo(name = "slug")
    var slug: String,

    @ColumnInfo(name = "category")
    var category: String,

    @ColumnInfo(name = "image")
    var image: String,

    @ColumnInfo(name = "price")
    var price: Float,

    @ColumnInfo(name = "discount")
    var discount: Float,

    @ColumnInfo(name = "unit")
    var unit: String

)
