package com.example.bozorbek_vol2.ui.main.profile.state

import com.example.bozorbek_vol2.network.main.network_services.profile.request.ProfileReadyPackageAutoOrder
import okhttp3.MultipartBody

sealed class ProfileStateEvent {

    class GetProfileInfo : ProfileStateEvent()

    data class GetProfileReadyPackages(val type:String) : ProfileStateEvent()

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

    data class SetReadyPackageId(val id:Int):ProfileStateEvent()

    data class SetReadyPackageToAutoOrder(val profileReadyPackageAutoOrder: ProfileReadyPackageAutoOrder): ProfileStateEvent()

    data class SetComplaints(val title:String, val text:String) : ProfileStateEvent()

    data class RemoveReadyPackageItem(val id:Int):ProfileStateEvent()

    class SetNotificationEvent : ProfileStateEvent()

    class GetProfileAutoOrderList : ProfileStateEvent()

    class None : ProfileStateEvent()

}
