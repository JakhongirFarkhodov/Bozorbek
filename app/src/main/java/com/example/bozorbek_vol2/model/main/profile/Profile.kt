package com.example.bozorbek_vol2.model.main.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profile_table")
data class Profile(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    val username:String,

    @ColumnInfo(name = "customer_phone")
    val customer_phone:Int,

    @ColumnInfo(name = "first_name")
    val first_name:String,

    @ColumnInfo(name = "last_name")
    val last_name:String,

    @ColumnInfo(name = "get_image")
    val get_image:String

)
