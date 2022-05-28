package com.example.bozorbek_vol2.di

import androidx.lifecycle.ViewModelProvider
import com.example.bozorbek_vol2.viewmodels.ViewModelProviderFactory
import dagger.Binds
import dagger.Module

@Module
abstract class ViewModelProviderFactoryModule {
    @Binds
    abstract fun bindsViewModelProviderFactory(viewModelProviderFactory: ViewModelProviderFactory): ViewModelProvider.Factory
}