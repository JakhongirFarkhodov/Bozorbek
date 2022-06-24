package com.example.bozorbek_vol2.persistance.main.home

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.home.HomeDiscountProducts
import com.example.bozorbek_vol2.model.main.home.HomeRandomProducts
import com.example.bozorbek_vol2.model.main.home.HomeSliderImage

@Dao
interface HomeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSliderImage(sliderImage: HomeSliderImage):Long

    @Query("SELECT * FROM slider_image_table")
    fun getAllSliderImages():LiveData<List<HomeSliderImage>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRandomProduct(homeRandomProducts: HomeRandomProducts):Long

    @Query("SELECT * FROM home_random_products")
    fun getAllRandomProducts():LiveData<List<HomeRandomProducts>>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiscountProduct(homeDiscountProducts: HomeDiscountProducts):Long

    @Query("SELECT * FROM home_discount_product_table")
    fun getAllDiscountProducts():LiveData<List<HomeDiscountProducts>>?

}