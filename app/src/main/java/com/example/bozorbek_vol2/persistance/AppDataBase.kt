package com.example.bozorbek_vol2.persistance

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.bozorbek_vol2.model.auth.AccountProperties
import com.example.bozorbek_vol2.model.auth.AuthToken
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.paket.Paket
import com.example.bozorbek_vol2.model.main.catalog.parametrs.product_owner.ProductOwner
import com.example.bozorbek_vol2.model.main.catalog.parametrs.sort.Sort
import com.example.bozorbek_vol2.model.main.profile.Profile
import com.example.bozorbek_vol2.model.main.profile.ProfileReadyPackages
import com.example.bozorbek_vol2.persistance.auth.AccountPropertiesDao
import com.example.bozorbek_vol2.persistance.auth.AuthTokenDao
import com.example.bozorbek_vol2.persistance.main.basket.BasketDao
import com.example.bozorbek_vol2.persistance.main.catalog.CatalogDao
import com.example.bozorbek_vol2.persistance.main.profile.ProfileDao

@Database(
    entities = [AccountProperties::class,
        AuthToken::class,
        Catalog::class,
        CatalogProduct::class,
        CatalogViewProduct::class,
        Sort::class,
        ProductOwner::class,
        Paket::class,
        BasketOrderProduct::class,
        Profile::class,
        ProfileReadyPackages::class], version = 27
)
abstract class AppDataBase : RoomDatabase() {

    //Auth
    abstract fun getAccountPropertiesDao(): AccountPropertiesDao
    abstract fun getAuthTokenDao(): AuthTokenDao

    //Main

    //Catalog
    abstract fun getCatalogDao(): CatalogDao

    //Basket
    abstract fun getBasketDao(): BasketDao

    //Profile
    abstract fun getProfileDao(): ProfileDao

    companion object {
        const val DATABASE_NAME = "app_db"
    }

}