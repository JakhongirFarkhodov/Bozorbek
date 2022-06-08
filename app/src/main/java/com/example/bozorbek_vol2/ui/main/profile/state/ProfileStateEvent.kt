package com.example.bozorbek_vol2.ui.main.profile.state

sealed class ProfileStateEvent {

    class GetProfileInfo : ProfileStateEvent()
    class GetProfileReadyPackages : ProfileStateEvent()
    data class UpdateProfilePassword(val old_password:String, val new_password:String, val confirm_new_password:String) : ProfileStateEvent()
    class None : ProfileStateEvent()

}
