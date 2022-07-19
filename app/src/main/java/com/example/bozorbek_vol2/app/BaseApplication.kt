package com.example.bozorbek_vol2.app

import com.example.bozorbek_vol2.di.DaggerAppComponent
import com.yariksoffice.lingver.Lingver
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication

class BaseApplication : DaggerApplication() {
    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().application(this).build()
    }

    override fun onCreate() {
        super.onCreate()
        Lingver.init(this)
    }
}