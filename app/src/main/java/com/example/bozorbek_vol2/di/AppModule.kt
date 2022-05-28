package com.example.bozorbek_vol2.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.persistance.AppDataBase
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.util.LiveDataCallAdapterFactory
import com.example.bozorbek_vol2.util.PreferenceKeys
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Singleton
    @Provides
    fun providesSharedPreferences(application: Application): SharedPreferences
    {
        return application.getSharedPreferences(PreferenceKeys.APP_PREFERENCES, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun providesSharedPrefEditor(sharedPreferences: SharedPreferences): SharedPreferences.Editor
    {
        return sharedPreferences.edit()
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder{
        return Retrofit.Builder()
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Constants.BASE_URL)
    }

    @Singleton
    @Provides
    fun providesAppDataBase(application: Application):AppDataBase
    {
        return Room.databaseBuilder(application, AppDataBase::class.java, AppDataBase.DATABASE_NAME).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun providesAccountPropertiesDao(dataBase:AppDataBase): AccountPropertiesDao
    {
        return dataBase.getAccountPropertiesDao()
    }

    @Singleton
    @Provides
    fun providesAuthTokenDao(dataBase: AppDataBase): AuthTokenDao
    {
        return dataBase.getAuthTokenDao()
    }

    @Singleton
    @Provides
    fun providesRequestOptions(): RequestOptions
    {
        return RequestOptions().placeholder(android.R.color.transparent).error(R.drawable.default_image)
    }

    @Singleton
    @Provides
    fun providesRequestManager(application: Application, requestOptions: RequestOptions): RequestManager
    {
        return Glide.with(application).setDefaultRequestOptions(requestOptions)
    }


}