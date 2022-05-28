package com.example.bozorbek_vol2.network.auth

import androidx.lifecycle.LiveData
import com.example.bozorbek_vol2.network.auth.network_services.request.LoginRequest
import com.example.bozorbek_vol2.network.auth.network_services.request.RegisterRequest
import com.example.bozorbek_vol2.network.auth.network_services.request.SetPasswordRequest
import com.example.bozorbek_vol2.network.auth.network_services.request.VerifySmsCodeRequest
import com.example.bozorbek_vol2.network.auth.network_services.response.LoginResponse
import com.example.bozorbek_vol2.network.auth.network_services.response.RegisterResponse
import com.example.bozorbek_vol2.network.auth.network_services.response.SetPasswordResponse
import com.example.bozorbek_vol2.network.auth.network_services.response.VerifySmsCodeResponse
import com.example.bozorbek_vol2.util.GenericApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {

    @POST("/customer/register/")
    fun registerRequest(@Body registerRequest: RegisterRequest):LiveData<GenericApiResponse<RegisterResponse>>

    @POST("/customer/verify/")
    fun verifySmsCodeRequest(@Body verifySmsCodeRequest: VerifySmsCodeRequest):LiveData<GenericApiResponse<VerifySmsCodeResponse>>

    @POST("/customer/set_password/")
    fun setPasswordRequest(@Body setPasswordRequest: SetPasswordRequest):LiveData<GenericApiResponse<SetPasswordResponse>>

    @POST("/customer/login/")
    fun loginRequest(@Body loginRequest: LoginRequest):LiveData<GenericApiResponse<LoginResponse>>

}