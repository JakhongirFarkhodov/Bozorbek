package com.example.bozorbek_vol2.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.request.RequestOptions
import com.example.bozorbek_vol2.R
import com.example.bozorbek_vol2.persistance.AppDataBase
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.session.SessionManager
import com.example.bozorbek_vol2.util.Constants
import com.example.bozorbek_vol2.util.LiveDataCallAdapterFactory
import com.example.bozorbek_vol2.util.PreferenceKeys
import dagger.Module
import dagger.Provides
import okhttp3.*
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class AppModule {
    private val TAG = Constants.LOG
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
    fun providesCache(application: Application):Cache
    {
        return Cache(File(application.cacheDir,"someIdentifier"),Constants.cacheSize)
    }

    @Singleton
    @Provides
    fun providesNetworkInterceptor():Interceptor{
        return object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "network interceptor: called.")

                val response = chain.proceed(chain.request())
                val cacheControl = CacheControl.Builder()
                    .maxAge(5,TimeUnit.SECONDS)
                    .build()

                return response.newBuilder()
                    .removeHeader(Constants.HEADER_PRAGMA)
                    .removeHeader(Constants.HEADER_CACHE_CONTROL)
                    .header(Constants.HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build()
            }

        }
    }

    @Singleton
    @Provides
    fun providesOfflineInterceptor(sessionManager: SessionManager):Interceptor
    {
        return object : Interceptor{
            override fun intercept(chain: Interceptor.Chain): Response {
                Log.d(TAG, "offline interceptor: called.")
                var request = chain.request()

                if (!sessionManager.isInternetAvailable())
                {
                    val cacheControl = CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()

                    request = request.newBuilder()
                        .removeHeader(Constants.HEADER_PRAGMA)
                        .removeHeader(Constants.HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
                }

                return chain.proceed(request)
            }

        }
    }

    @Singleton
    @Provides
    fun providesOkHttpClient(sessionManager: SessionManager):OkHttpClient
    {
        return OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1,TimeUnit.MINUTES)
            .writeTimeout(15, TimeUnit.MINUTES)
            .addInterceptor(providesNetworkInterceptor())
            .addInterceptor(providesOfflineInterceptor(sessionManager))
            .build()
    }

    @Singleton
    @Provides
    fun providesRetrofitBuilder(okHttpClient: OkHttpClient): Retrofit.Builder{
        return Retrofit.Builder()
            .addCallAdapterFactory(LiveDataCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
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