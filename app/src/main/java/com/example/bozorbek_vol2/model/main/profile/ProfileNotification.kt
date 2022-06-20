package com.example.bozorbek_vol2.model.main.profile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notification_table")
data class ProfileNotification(

    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    val id:Int,

    @ColumnInfo(name = "title")
    val title:String,

    @ColumnInfo(name = "content")
    val content:String,

    @ColumnInfo(name = "to_customer")
    val to_customer:Int,

    @ColumnInfo(name = "created_at")
    val created_at:String,

    @ColumnInfo(name = "is_read")
    val is_read:Boolean

)
