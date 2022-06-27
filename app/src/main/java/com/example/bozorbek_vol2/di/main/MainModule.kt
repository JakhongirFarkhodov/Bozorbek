package com.example.bozorbek_vol2.di.main

import android.content.SharedPreferences
import com.example.bozorbek_vol2.network.main.MainApiServices
import com.example.bozorbek_vol2.persistance.AppDataBase
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.persistance.main.basket.BasketDao
import com.example.bozorbek_vol2.persistance.main.catalog.CatalogDao
import com.example.bozorbek_vol2.persistance.main.home.HomeDao
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao
import com.example.bozorbek_vol2.persistance.main.search.SearchDao
import com.example.bozorbek_vol2.repository.main.basket.BasketRepository
import com.example.bozorbek_vol2.repository.main.catalog.CatalogRepository
import com.example.bozorbek_vol2.repository.main.home.HomeRepository
import com.example.bozorbek_vol2.repository.main.profile.ProfileRepository
import com.example.bozorbek_vol2.repository.main.search.SearchRepository
import com.example.bozorbek_vol2.session.SessionManager
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module
class MainModule {

    @MainScope
    @Provides
    fun providesMainApiServices(retrofit: Retrofit.Builder): MainApiServices {
        return retrofit.build().create(MainApiServices::class.java)
    }

    @MainScope
    @Provides
    fun providesHomeRepository(sessionManager: SessionManager, homeDao: HomeDao, apiServices: MainApiServices, catalogDao: CatalogDao):HomeRepository{
        return HomeRepository(sessionManager, homeDao, apiServices, catalogDao)
    }

    @MainScope
    @Provides
    fun providesHomeDao(dataBase: AppDataBase):HomeDao
    {
        return dataBase.getHomeDao()
    }

    @MainScope
    @Provides
    fun providesCatalogRepository(sessionManager: SessionManager, catalogDao: CatalogDao, apiServices: MainApiServices):CatalogRepository{
        return CatalogRepository(sessionManager, catalogDao, apiServices)
    }

    @MainScope
    @Provides
    fun providesCatalogDao(dataBase: AppDataBase):CatalogDao
    {
        return dataBase.getCatalogDao()
    }


    @MainScope
    @Provides
    fun providesSearchRepository(
        sessionManager: SessionManager,
        accountPropertiesDao: AccountPropertiesDao,
        authTokenDao: AuthTokenDao,
        sharedPreferences: SharedPreferences,
        searchDao: SearchDao,
        apiServices: MainApiServices,
        catalogDao: CatalogDao
    ): SearchRepository {
        return SearchRepository(
            accountPropertiesDao,
            authTokenDao,
            sessionManager,
            sharedPreferences,
            apiServices,
            searchDao, catalogDao
        )
    }

    @MainScope
    @Provides
    fun providesSearchDao(dataBase: AppDataBase):SearchDao
    {
        return dataBase.getSearchDao()
    }

    @MainScope
    @Provides
    fun providesBasketRepository(sessionManager: SessionManager, apiServices: MainApiServices, basketDao: BasketDao, profileDao: ProfileDao):BasketRepository
    {
        return BasketRepository(sessionManager, apiServices, basketDao, profileDao)
    }

    @MainScope
    @Provides
    fun providesBasketDao(dataBase: AppDataBase):BasketDao
    {
        return dataBase.getBasketDao()
    }

    @MainScope
    @Provides
    fun providesProfileRepository(sessionManager: SessionManager, apiServices: MainApiServices, profileDao: ProfileDao):ProfileRepository
    {
        return ProfileRepository(sessionManager, apiServices, profileDao)
    }

    @MainScope
    @Provides
    fun providesProfileDao(dataBase: AppDataBase):ProfileDao
    {
        return dataBase.getProfileDao()
    }


}