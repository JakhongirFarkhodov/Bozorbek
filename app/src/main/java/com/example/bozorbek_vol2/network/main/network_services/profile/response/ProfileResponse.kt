package com.example.bozorbek_vol2.network.main.network_services.profile.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class ProfileResponse(
    @SerializedName("username")
    @Expose
    var username: String,

    @SerializedName("customer_phone")
    @Expose
    var customerPhone: Int,

    @SerializedName("gender")
    @Expose
    var gender: Any,

    @SerializedName("first_name")
    @Expose
    var firstName: String,

    @SerializedName("last_name")
    @Expose
    var lastName: String,

    @SerializedName("get_image")
    @Expose
    var getImage: String
)
