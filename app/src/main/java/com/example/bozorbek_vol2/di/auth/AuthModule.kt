package com.example.bozorbek_vol2.di.auth

import android.content.SharedPreferences
import com.example.bozorbek_vol2.network.auth.AuthApiService
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.repository.auth.AuthRepository
import com.example.bozorbek_vol2.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class AuthModule {

    @AuthScope
    @Provides
    fun providesAuthApiService(retrofit: Retrofit.Builder): AuthApiService {
        return retrofit.build().create(AuthApiService::class.java)
    }

    @AuthScope
    @Provides
    fun providesAuthRepository(
        apiService: AuthApiService,
        accountPropertiesDao: AccountPropertiesDao,
        authTokenDao: AuthTokenDao,
        sessionManager: SessionManager,
        sharedPreEditor: SharedPreferences.Editor
    ): AuthRepository {
        return AuthRepository(apiService,accountPropertiesDao, authTokenDao, sessionManager, sharedPreEditor)
    }

}