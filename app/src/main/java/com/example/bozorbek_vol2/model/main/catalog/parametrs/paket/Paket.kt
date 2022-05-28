package com.example.bozorbek_vol2.model.main.catalog.parametrs.paket

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "paket_table")
data class Paket(

    @ColumnInfo(name = "paket_id")
    val paket_id:Int,

    @ColumnInfo(name = "paket_name")
    val paket_name:String,

    @ColumnInfo(name = "paket_value")
    val paket_value:String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "parameter_value_id")
    val paket_value_id:Int


)
