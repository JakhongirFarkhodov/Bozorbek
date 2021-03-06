package com.example.bozorbek_vol2.persistance.main.basket

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.basket.BasketOrderProduct

@Dao
interface BasketDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBasketOrderProduct(basketOrderProduct: BasketOrderProduct):Long

    @Query("SELECT * FROM basket_order_product_table ORDER BY basket_id DESC")
    fun getListOfBasketOrderProduct(): LiveData<List<BasketOrderProduct>>?

    @Query("DELETE FROM basket_order_product_table")
    suspend fun deleteAllListOfBasketOrderProduct()

}