package com.example.bozorbek_vol2.model.main.catalog.parametrs.sort

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sort_table")
data class Sort(
    @ColumnInfo(name = "sort_id")
    val sort_id: Int,

    @ColumnInfo(name = "sort_name")
    val sort_name: String,

    @ColumnInfo(name = "sort_value")
    val sort_value: String,

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "sort_value_id")
    val sort_value_id: Int
)