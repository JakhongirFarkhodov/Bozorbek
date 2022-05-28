package com.example.bozorbek_vol2.di.auth

import com.example.bozorbek_vol2.ui.auth.fragments.CreatePasswordFragment
import com.example.bozorbek_vol2.ui.auth.fragments.LoginFragment
import com.example.bozorbek_vol2.ui.auth.fragments.RegisterFragment
import com.example.bozorbek_vol2.ui.auth.fragments.SmsVerificationFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AuthFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributesLoginFragment():LoginFragment

    @ContributesAndroidInjector
    abstract fun contributesRegistrationFragment():RegisterFragment

    @ContributesAndroidInjector
    abstract fun contributesSmsVerificationFragment():SmsVerificationFragment

    @ContributesAndroidInjector
    abstract fun contributesCreatePasswordFragment():CreatePasswordFragment


}