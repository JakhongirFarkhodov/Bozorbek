package com.example.bozorbek_vol2.di.auth

import androidx.lifecycle.ViewModel
import com.example.bozorbek_vol2.di.ViewModelKey
import com.example.bozorbek_vol2.ui.auth.viewmodel.AuthViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AuthViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(AuthViewModel::class)
    abstract fun bindsAuthViewModel(authViewModel: AuthViewModel):ViewModel

}