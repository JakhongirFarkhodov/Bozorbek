package com.example.bozorbek_vol2.persistance.main.search

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.search.SearchProduct

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchProduct(searchProduct: SearchProduct):Long

    @Query("SELECT * FROM search_product_table")
    fun getAllSearchProduct():LiveData<List<SearchProduct>>?

    @Query("DELETE FROM search_product_table")
    suspend fun deleteAllSearchProduct()

}