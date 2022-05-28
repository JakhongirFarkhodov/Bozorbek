package com.example.bozorbek_vol2.ui.auth.state

data class AuthViewState(
    var loginValue: LoginValue? = LoginValue(),
    var registrationValue: RegistrationValue? = RegistrationValue(),
    var verifySmsCodeValue: VerifySmsCodeValue? = VerifySmsCodeValue(),
    var passwordValue: PasswordValue? = PasswordValue()
)

data class RegistrationValue(
    val first_name: String? = null,
    val phone_number: String? = null,
) {
    class RegistrationError {
        companion object {

            fun first_name_empty(): String {
                return "Поле для ввода имени и фамилии пуста. Пожалуйста заполните данное поле."
            }

            fun phone_number_empty(): String {
                return "Поле для ввода номера телефона пуста. Пожалуйста заполните данное поле."
            }

            fun filed_are_empty(): String {
                return "Поля для ввода имени и фамилии а также для номер телефона пусты. Пожалуйста заполните данные поля."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun checkRegistrationValue(): String {
        if (first_name.isNullOrBlank() && phone_number.isNullOrBlank()) {
            return RegistrationError.filed_are_empty()
        }
        if (first_name.isNullOrBlank()) {
            return RegistrationError.first_name_empty()
        }
        if (phone_number.isNullOrBlank()) {
            return RegistrationError.phone_number_empty()
        }
        return RegistrationError.none()
    }

}

data class VerifySmsCodeValue(
    val sms_code: String? = null,
    val responseMessage:String? = null
) {
    class VerifySmsCodeError {
        companion object {
            fun sms_code_empty(): String {
                return "Поле для ввода кода подтверждения пуста. Пожалуйста введите код подтверждения."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun checkVerifySmsCode(): String {
        if (sms_code.isNullOrBlank()) {
            return VerifySmsCodeError.sms_code_empty()
        }
        return VerifySmsCodeError.none()
    }
}

data class PasswordValue(
    val password: String? = null,
    val confirm_password: String? = null,
    val username:String? = null
) {
    class PasswordError {
        companion object {
            fun password_field_empty(): String {
                return "Поле для ввода пароля пуста. Пожалуйста введите свой пароль."
            }

            fun confirm_password_field_empty(): String {
                return "Поле для ввода подтверждения пароля пуста. Пожалуйста введите пароль подтверждения."
            }

            fun password_fields_are_empty(): String {
                return "Поля для ввода пароля и пароля потверждения пусты. Пожалуйста заполните данные поля."
            }

            fun confirm_password():String
            {
                return "Пароли не совпадают."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun checkPasswordValue(): String {
        if (password.isNullOrBlank() && confirm_password.isNullOrBlank()) {
            return PasswordError.password_fields_are_empty()
        }
        if (password.isNullOrBlank()) {
            return PasswordError.password_field_empty()

        }
        if (confirm_password.isNullOrBlank()) {
            return PasswordError.confirm_password_field_empty()
        }
        if (!password.equals(confirm_password))
        {
            return PasswordError.confirm_password()
        }
        return PasswordError.none()
    }
}

data class LoginValue(
    val phone_number: String? = null,
    val password: String? = null,
    val access_token:String? = null,
    val refreshToken:String? = null
) {
    class LoginError {
        companion object {
            fun phone_number_field_empty(): String {
                return "Поле для ввода логина пользывателя пуста. Пожалуйста введите свой логин."
            }

            fun password_field_empty(): String {
                return "Поле для ввода пароля пуста. ожайлуйта ввелите свой пароль."
            }

            fun login_value_are_empty():String{
                return "Поля для ввода логина и пароля пусты. Пожалуйста заполните данные поля."
            }

            fun none():String{
                return "None"
            }
        }
    }

    fun checkLoginFields():String{
        if (phone_number.isNullOrBlank() && password.isNullOrBlank())
        {
            return LoginError.login_value_are_empty()
        }
        if (phone_number.isNullOrBlank())
        {
            return LoginError.phone_number_field_empty()
        }
        if (password.isNullOrBlank())
        {
            return LoginError.password_field_empty()

        }
        return LoginError.none()
    }
}