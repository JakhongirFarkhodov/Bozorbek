package com.example.bozorbek_vol2.di

import com.example.bozorbek_vol2.di.auth.AuthFragmentBuildersModule
import com.example.bozorbek_vol2.di.auth.AuthModule
import com.example.bozorbek_vol2.di.auth.AuthScope
import com.example.bozorbek_vol2.di.auth.AuthViewModelModule
import com.example.bozorbek_vol2.di.main.MainFragmentBuildersModule
import com.example.bozorbek_vol2.di.main.MainModule
import com.example.bozorbek_vol2.di.main.MainScope
import com.example.bozorbek_vol2.di.main.MainViewModelModule
import com.example.bozorbek_vol2.ui.auth.AuthActivity
import com.example.bozorbek_vol2.ui.main.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuildersModule {

    @AuthScope
    @ContributesAndroidInjector(modules = [AuthFragmentBuildersModule::class, AuthModule::class, AuthViewModelModule::class])
    abstract fun contributesAuthActivity():AuthActivity

    @MainScope
    @ContributesAndroidInjector(modules = [MainFragmentBuildersModule::class, MainModule::class, MainViewModelModule::class])
    abstract fun contributesMainActivity():MainActivity

}