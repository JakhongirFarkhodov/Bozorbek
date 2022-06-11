package com.example.bozorbek_vol2.ui.main.catalog.state

sealed class CatalogStateEvent {

    class GetCatalogListOfData : CatalogStateEvent()

    data class GetCatalogProductListOfData(val slug: String) : CatalogStateEvent()

    data class GetCatalogViewProductListOfData(val category_slug:String, val product_slug:String):CatalogStateEvent()

    data class GetCatalogViewProductBySortValue(val sort_value:String):CatalogStateEvent()

    data class AddCatalogOrderItem(val product_item_id:String, val quantity:Int, val unit:String, val size:String, val sortValue:String):CatalogStateEvent()

    class None : CatalogStateEvent()

}
