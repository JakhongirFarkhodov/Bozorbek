package com.example.bozorbek_vol2.model.main.catalog.parametrs.product_owner

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product_owner_table")
data class ProductOwner(

    @ColumnInfo(name = "product_owner_id")
    val product_owner_id:Int,

    @ColumnInfo(name = "product_owner_name")
    val product_owner_name:String,

    @ColumnInfo(name = "product_owner_value")
    val product_owner_value:String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "product_owner_value_id")
    val product_owner_value_id:Int

)
