package com.example.bozorbek_vol2.model.main.catalog.parametrs

import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.paket.Paket
import com.example.bozorbek_vol2.model.main.catalog.parametrs.product_owner.ProductOwner
import com.example.bozorbek_vol2.model.main.catalog.parametrs.sort.Sort

data class ParametersValue(
    val paket: List<Paket> = ArrayList(),
    val productOwner: List<ProductOwner> = ArrayList(),
    val sort: List<Sort> = ArrayList(),
    val items:List<CatalogViewProduct> = ArrayList()
)
