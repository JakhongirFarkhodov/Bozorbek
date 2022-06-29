package com.example.bozorbek_vol2.di

import android.app.Application
import com.example.bozorbek_vol2.app.BaseApplication
import com.example.bozorbek_vol2.session.SessionManager
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import dagger.android.AndroidInjector
import javax.inject.Singleton

@Singleton
@Component(
    modules = [AndroidInjectionModule::class,
        ActivityBuildersModule::class,
        AppModule::class,
        ViewModelProviderFactoryModule::class]
)
interface AppComponent : AndroidInjector<BaseApplication> {

    val sessionManager: SessionManager



    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent

    }
}