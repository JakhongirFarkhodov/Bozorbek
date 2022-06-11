package com.example.bozorbek_vol2.ui.main.profile.state

import okhttp3.MultipartBody

sealed class ProfileStateEvent {

    class GetProfileInfo : ProfileStateEvent()
    class GetProfileReadyPackages : ProfileStateEvent()
    data class UploadProfileImage(val image:MultipartBody.Part):ProfileStateEvent()
    data class AddItemReadyPackagesToBasket(val package_item_id:String):ProfileStateEvent()
    data class GetAllActiveOrHistoryOrder(
        val UNAPPROVED:String?,
        val APPROVED:String?,
        val COLLECTING:String?,
        val COLLECTED:String?,
        val DELIVERING:String?,
        val DELIVERED:String?,
        val CANCELLED:String?
    ) : ProfileStateEvent()
    data class UpdateProfilePassword(val old_password:String, val new_password:String, val confirm_new_password:String) : ProfileStateEvent()
    class None : ProfileStateEvent()

}
