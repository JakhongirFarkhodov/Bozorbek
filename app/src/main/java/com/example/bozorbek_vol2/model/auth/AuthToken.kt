package com.example.bozorbek_vol2.model.auth

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "auth_token", foreignKeys = [ForeignKey(
    entity = AccountProperties::class,
    parentColumns = ["phone_number"],
    childColumns = ["account_phone_number"],
    onDelete = ForeignKey.CASCADE
)])
data class AuthToken(

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "account_phone_number")
    val account_phone_number:String = "",

    @SerializedName("access")
    @ColumnInfo(name = "access_token")
    @Expose
    val access_token:String? = null,

    @SerializedName("refresh")
    @ColumnInfo(name = "refresh")
    @Expose
    val refresh_token:String? = null

)
