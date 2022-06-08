package com.example.bozorbek_vol2.ui.main.profile.state

import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackages

data class ProfileViewState(

    var profile: Profile? = null,
    var readyPackagesList: List<ProfileReadyPackages>? = ArrayList<ProfileReadyPackages>()

)

data class ProfilePasswordValue(
    val old_password: String? = null,
    val password: String? = null,
    val confirm_password: String? = null

) {
    class PasswordError {
        companion object {
            fun old_password_field_empty(): String {
                return "Поле для ввода cтарого пароля пуста. Пожалуйста введите старый пароль."
            }

            fun new_password_field_empty(): String {
                return "Поле для ввода нового пароля пуста. Пожалуйста введите новый пароль."
            }

            fun confirm_password_field_empty(): String {
                return "Поле для ввода подтверждения новго пароля пуста. Пожалуйста введите новый пароль подтверждения."
            }

            fun password_fields_are_empty(): String {
                return "Поля для ввода пароля и пароля потверждения пусты. Пожалуйста заполните данные поля."
            }

            fun confirm_password(): String {
                return "Пароли не совпадают."
            }

            fun none(): String {
                return "None"
            }
        }
    }

    fun checkPasswordValue(): String {
        if (password.isNullOrBlank() && confirm_password.isNullOrBlank() && old_password.isNullOrBlank()) {
            return PasswordError.password_fields_are_empty()
        }

        if (old_password.isNullOrBlank()) {
            return PasswordError.old_password_field_empty()
        }

        if (password.isNullOrBlank()) {
            return PasswordError.new_password_field_empty()
        }

        if (confirm_password.isNullOrBlank()) {
            return PasswordError.confirm_password_field_empty()
        }

        if (!password.equals(confirm_password)) {
            return PasswordError.confirm_password()
        }
        return PasswordError.none()
    }
}
