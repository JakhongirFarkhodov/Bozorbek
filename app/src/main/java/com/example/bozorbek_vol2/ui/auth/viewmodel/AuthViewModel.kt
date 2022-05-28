package com.example.bozorbek_vol2.ui.auth.viewmodel

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.repository.auth.AuthRepository
import com.example.bozorbek_vol2.ui.BaseViewModel
import com.example.bozorbek_vol2.ui.DataState
import com.example.bozorbek_vol2.ui.auth.state.*
import com.example.bozorbek_vol2.util.AbsentLiveData
import javax.inject.Inject

class AuthViewModel
    @Inject
    constructor(
        val authRepository: AuthRepository
    )
    : BaseViewModel<AuthStateEvent, AuthViewState>() {
    override fun initNewViewState(): AuthViewState {
        return AuthViewState()
    }

    override fun handleStateEvent(stateEvent: AuthStateEvent): LiveData<DataState<AuthViewState>> {
        when(stateEvent)
        {
            is AuthStateEvent.LoginStateEvent ->{
                return authRepository.loginResponse(username = stateEvent.phone_number, password = stateEvent.password)
            }
            is AuthStateEvent.RegistrationStateEvent ->{
                return authRepository.registrationResponse(first_name = stateEvent.first_name, phone_number = stateEvent.phone_number)
            }
            is AuthStateEvent.VerifySmsCodeStateEvent ->{
                return authRepository.verifySmsCodeResponse(sms_code = stateEvent.sms_code, phone_number = stateEvent.phone_number)
            }
            is AuthStateEvent.SetPasswordStateEvent ->{
                return authRepository.setPasswordResponse(phone_number = stateEvent.phone_number, sms_code = stateEvent.sms_code, first_name = stateEvent.first_name, password = stateEvent.password, confirm_password = stateEvent.confirm_password)
            }
            is AuthStateEvent.RefreshTokenStateEvent ->{
                return AbsentLiveData.create()
            }
            is AuthStateEvent.None ->{
                return AbsentLiveData.create()
            }
        }
    }

    fun setRegistrationValue(registrationValue: RegistrationValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.registrationValue == registrationValue)
        {
            return
        }
        update.registrationValue = registrationValue
        _viewState.value = update
    }

    fun setSmsVerifyCodeValue(verifySmsCodeValue: VerifySmsCodeValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.verifySmsCodeValue == verifySmsCodeValue)
        {
            return
        }
        update.verifySmsCodeValue = verifySmsCodeValue
        _viewState.value = update
    }

    fun setPasswordValue(passwordValue: PasswordValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.passwordValue == passwordValue)
        {
            return
        }
        update.passwordValue = passwordValue
        _viewState.value = update
    }

    fun setLoginValue(loginValue: LoginValue)
    {
        val update = getCurrentViewStateOrCreateNew()
        if (update.loginValue == loginValue)
        {
            return
        }
        update.loginValue = loginValue
        _viewState.value = update
    }
}