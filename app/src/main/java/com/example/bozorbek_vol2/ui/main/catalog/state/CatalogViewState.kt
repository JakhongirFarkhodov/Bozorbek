package com.example.bozorbek_vol2.ui.main.catalog.state

import com.example.bozorbek_vol2.model.main.catalog.Catalog
import com.example.bozorbek_vol2.model.main.catalog.CatalogProduct
import com.example.bozorbek_vol2.model.main.catalog.CatalogViewProduct
import com.example.bozorbek_vol2.model.main.catalog.parametrs.ParametersValue
import com.example.bozorbek_vol2.ui.main.catalog.fragment.model.CatalogModel

data class CatalogViewState(
    val catalogList: CatalogList = CatalogList(),
    val catalogProductList: CatalogProductList = CatalogProductList(),
    var catalogModel:CatalogModel? = CatalogModel(),
    var parametersValue: ParametersValue? = ParametersValue(),
    var message:String? = null
)

data class CatalogList(
    var list: List<Catalog>? = ArrayList()
)

data class CatalogProductList(
    var list: List<CatalogProduct>? = ArrayList()
)

data class CatalogViewProductList(
    var list:List<CatalogViewProduct>? = ArrayList()
)