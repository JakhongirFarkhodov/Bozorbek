package com.example.bozorbek_vol2.di.main

import com.example.bozorbek_vol2.ui.main.basket.fragment.BasketFragment
import com.example.bozorbek_vol2.ui.main.basket.fragment.BasketMapFragment
import com.example.bozorbek_vol2.ui.main.catalog.fragment.CatalogFragment
import com.example.bozorbek_vol2.ui.main.catalog.fragment.CatalogProductFragment
import com.example.bozorbek_vol2.ui.main.catalog.fragment.CatalogViewProductFragment
import com.example.bozorbek_vol2.ui.main.home.fragment.HomeFragment
import com.example.bozorbek_vol2.ui.main.home.fragment.HomeProductFragment
import com.example.bozorbek_vol2.ui.main.home.fragment.HomeViewProductFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.ProfileFragment
import com.example.bozorbek_vol2.ui.main.search.fragment.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributesHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributesProductsFragment(): HomeProductFragment

    @ContributesAndroidInjector
    abstract fun contributesViewProductsFragment(): HomeViewProductFragment

    @ContributesAndroidInjector
    abstract fun contributesCatalogFragment(): CatalogFragment

    @ContributesAndroidInjector
    abstract fun contributesViewCatalogFragment(): CatalogProductFragment

    @ContributesAndroidInjector
    abstract fun contributesViewCatalogProductFragment(): CatalogViewProductFragment

    @ContributesAndroidInjector
    abstract fun contributesSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributesBasketFragment(): BasketFragment

    @ContributesAndroidInjector
    abstract fun contributesBasketMapFragment() : BasketMapFragment

    @ContributesAndroidInjector
    abstract fun contributesProfileFragment(): ProfileFragment


}