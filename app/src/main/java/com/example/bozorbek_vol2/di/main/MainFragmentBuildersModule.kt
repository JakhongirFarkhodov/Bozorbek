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
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.*
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.fragment.ActiveOrderFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.active_orders.fragment.HistoryOrderFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.PopUpListOfReadyPackagesProductsFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.PopUpOfAuthOrderFragment
import com.example.bozorbek_vol2.ui.main.profile.fragment.menu.ready_packages.fragments.PopUpSuccessfullySavedReadyPackageInAutoOrderFragment
import com.example.bozorbek_vol2.ui.main.search.fragment.SearchCatalogViewProductFragment
import com.example.bozorbek_vol2.ui.main.search.fragment.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainFragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributesPopUpAutoOrderFragment():PopUpOfAuthOrderFragment

    @ContributesAndroidInjector
    abstract fun contributesPopUpListOfReadyPackagesProductsFragment(): PopUpListOfReadyPackagesProductsFragment

    @ContributesAndroidInjector
    abstract fun contributesPopUpSuccessfullySavedReadyPackageInAutoOrderFragment():PopUpSuccessfullySavedReadyPackageInAutoOrderFragment

    //Home
    @ContributesAndroidInjector
    abstract fun contributesHomeFragment(): HomeFragment

    @ContributesAndroidInjector
    abstract fun contributesProductsFragment(): HomeProductFragment

    @ContributesAndroidInjector
    abstract fun contributesViewProductsFragment(): HomeViewProductFragment

    //Catalog
    @ContributesAndroidInjector
    abstract fun contributesCatalogFragment(): CatalogFragment

    @ContributesAndroidInjector
    abstract fun contributesViewCatalogFragment(): CatalogProductFragment

    @ContributesAndroidInjector
    abstract fun contributesViewCatalogProductFragment(): CatalogViewProductFragment

    //Search
    @ContributesAndroidInjector
    abstract fun contributesSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributesSearchCatalogViewProductFragment():SearchCatalogViewProductFragment

    //Basket
    @ContributesAndroidInjector
    abstract fun contributesBasketFragment(): BasketFragment

    @ContributesAndroidInjector
    abstract fun contributesBasketMapFragment() : BasketMapFragment


    //Profile
    @ContributesAndroidInjector
    abstract fun contributesProfileFragment(): ProfileFragment


    //Profile Menu
    @ContributesAndroidInjector
    abstract fun contributesSearchHistoryFragment():SearchHistoryFragment


    @ContributesAndroidInjector
    abstract fun contributesNotificationFragment():NotificationFragment

    @ContributesAndroidInjector
    abstract fun contributesOrderHistoryFragment():OrderHistoryFragment

    @ContributesAndroidInjector
    abstract fun contributesActiveOrderFragment() : ActiveOrderFragment

    @ContributesAndroidInjector
    abstract fun contributesHistoryOrderFragment() : HistoryOrderFragment

    @ContributesAndroidInjector
    abstract fun contributesCompanyInfoFragment():CompanyInfoFragment

    @ContributesAndroidInjector
    abstract fun contributesSecuritySettingsFragment():SecuritySettingsFragment

    @ContributesAndroidInjector
    abstract fun contributesComplaintsFragment():ComplaintsFragment

    //Ready Packages
    @ContributesAndroidInjector
    abstract fun contributesReadyPackagesFragment():ReadyPackagesFragment

    @ContributesAndroidInjector
    abstract fun contributesAutoOrderFragment():AutoOrderFragment

}