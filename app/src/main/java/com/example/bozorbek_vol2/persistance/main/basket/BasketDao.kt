package com.example.bozorbek_vol2.persistance.main.basket

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct

@Dao
interface BasketDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBasketOrderProduct(basketOrderProduct: BasketOrderProduct):Long

    @Query("SELECT * FROM basket_order_product_table")
    fun getListOfBasketOrderProduct(): LiveData<List<BasketOrderProduct>>?

}