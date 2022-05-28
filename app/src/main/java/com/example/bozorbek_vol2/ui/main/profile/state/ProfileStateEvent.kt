package com.example.bozorbek_vol2.ui.main.profile.state

sealed class ProfileStateEvent {

    class GetProfileInfo : ProfileStateEvent()
    class None : ProfileStateEvent()

}
