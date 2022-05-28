package com.example.bozorbek_vol2.ui.auth.state

sealed class AuthStateEvent{

    data class LoginStateEvent(val phone_number:String, val password:String) : AuthStateEvent()

    data class RegistrationStateEvent(val first_name: String, val phone_number: String) : AuthStateEvent()

    data class VerifySmsCodeStateEvent(val phone_number: String, val sms_code:String) : AuthStateEvent()

    data class SetPasswordStateEvent(val phone_number: String, val sms_code: String, val password: String, val confirm_password:String, val first_name:String) : AuthStateEvent()

    data class RefreshTokenStateEvent(val refresh_token:String) : AuthStateEvent()

    class None : AuthStateEvent()

}
