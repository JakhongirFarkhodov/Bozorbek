package com.example.bozorbek_vol2.model.auth

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "account_properties")
data class AccountProperties(

    @PrimaryKey(autoGenerate = false)
    @SerializedName("username")
    @ColumnInfo(name = "phone_number")
    @Expose
    val phone_number:String,

    @SerializedName("first_name")
    @ColumnInfo(name = "username")
    @Expose
    val username: String

)