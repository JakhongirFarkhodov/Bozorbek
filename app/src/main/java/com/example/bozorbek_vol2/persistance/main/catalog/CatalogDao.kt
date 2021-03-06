package com.example.bozorbek_vol2.persistance.main.catalog

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.paket.Paket
import com.example.bozorbek_vol2.model.main.catalog.parametrs.product_owner.ProductOwner
import com.example.bozorbek_vol2.model.main.catalog.parametrs.sort.Sort

@Dao
interface CatalogDao {

    //Catalog
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalog(catalog: Catalog):Long

    @Query("SELECT * FROM catalog_table")
    fun getListOfCatalog():LiveData<List<Catalog>>?


    //Catalog Product
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalogProducts(catalogProduct: CatalogProduct):Long

    @Query("DELETE FROM catalog_product_table")
    suspend fun deleteAllItemCatalogProductTable()

    @Query("SELECT * FROM catalog_product_table")
    fun getListOfCatalogProduct():LiveData<List<CatalogProduct>>?

    //Catalog Paket
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPaket(paket: Paket):Long

    @Query("DELETE FROM paket_table")
    suspend fun deleteAllPaketDate()

    @Query("SELECT * FROM paket_table")
    fun getAllPaketData():LiveData<List<Paket>>?

    //Catalog Product owner data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProductOwnerData(productOwner: ProductOwner):Long

    @Query("DELETE FROM product_owner_table")
    suspend fun deleteAllProductOwnerData()

    @Query("SELECT * FROM product_owner_table")
    fun getALlProductOwnerData():LiveData<List<ProductOwner>>?

    //Catalog Sort data
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSortData(sort: Sort):Long

    @Query("DELETE FROM sort_table")
    suspend fun deleteAllSortData()

    @Query("SELECT * FROM sort_table")
    fun getAllSortData():LiveData<List<Sort>>?

    //Catalog CatalogViewProduct
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCatalogViewProduct(catalogViewProduct: CatalogViewProduct):Long

    @Query("DELETE FROM catalog_view_product_table")
    suspend fun deleteAllItemCatalogViewProductTable()

    @Query("SELECT * FROM catalog_view_product_table")
    fun getAllCatalogViewProduct():LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value")
    fun getCatalogViewProductBySortValue(sort_value:String):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value")
    fun getCatalogViewProductBySortAndProductOwnerValue(sort_value: String, productOwner_value:String):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value AND paket_value ==:paket_value")
    fun getCatalogViewProductBySortAndProductOwnerAndPaketValue(sort_value: String, productOwner_value: String, paket_value:String):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value AND paket_value ==:paket_value AND in_gramme ==:in_gramme")
    fun getCatalogViewProductByGramme(sort_value: String, productOwner_value: String, paket_value:String, in_gramme:Boolean):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value AND paket_value ==:paket_value AND in_piece ==:in_piece")
    fun getCatalogViewProductByPiece(sort_value: String, productOwner_value: String, paket_value:String, in_piece:Boolean):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value AND paket_value ==:paket_value AND in_piece ==:in_piece AND in_gramme ==:in_gramme AND large ==:large")
    fun getCatalogViewProductBySizeLarge(sort_value: String, productOwner_value: String, paket_value:String, in_gramme: Boolean, in_piece:Boolean, large:Boolean):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value AND paket_value ==:paket_value AND in_piece ==:in_piece AND in_gramme ==:in_gramme AND middle ==:middle")
    fun getCatalogViewProductBySizeMiddle(sort_value: String, productOwner_value: String, paket_value:String, in_gramme: Boolean, in_piece:Boolean, middle:Boolean):LiveData<List<CatalogViewProduct>>?

    @Query("SELECT * FROM catalog_view_product_table WHERE sort_value ==:sort_value AND product_owner_value ==:productOwner_value AND paket_value ==:paket_value AND in_piece ==:in_piece AND in_gramme ==:in_gramme AND small ==:small")
    fun getCatalogViewProductBySizeSmall(sort_value: String, productOwner_value: String, paket_value:String, in_gramme: Boolean, in_piece:Boolean, small:Boolean):LiveData<List<CatalogViewProduct>>?





}