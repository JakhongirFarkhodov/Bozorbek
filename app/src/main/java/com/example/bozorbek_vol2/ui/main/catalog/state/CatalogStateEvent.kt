package com.example.bozorbek_vol2.ui.main.catalog.state

sealed class CatalogStateEvent {

    class GetCatalogListOfData() : CatalogStateEvent()

    data class GetCatalogProductListOfData(val slug: String) : CatalogStateEvent()

    data class SetCatalogViewProductsData(val category_slug:String, val product_slug:String):CatalogStateEvent()

    data class GetCatalogViewProductListOfData(val category_slug:String, val product_slug:String):CatalogStateEvent()

    data class GetCatalogViewProductBySortValue(val sort_value:String):CatalogStateEvent()

    data class GetCatalogViewProductBySortAndProductOwnerValue(val sort_value:String, val productOwner_value:String):CatalogStateEvent()

    data class GetCatalogViewProductBySortAndProductOwnerAndPaketValue(val sort_value:String, val productOwner_value:String, val paket_value:String):CatalogStateEvent()

    data class GetCatalogViewProductByGramme(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean) : CatalogStateEvent()

    data class GetCatalogViewProductByPiece(val sort_value:String, val productOwner_value:String, val paket_value:String, val piece:Boolean) : CatalogStateEvent()

    data class GetCatalogViewProductBySizeLarge(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val large:Boolean) : CatalogStateEvent()
    data class GetCatalogViewProductBySizeMiddle(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val middle:Boolean) : CatalogStateEvent()
    data class GetCatalogViewProductBySizeSmall(val sort_value:String, val productOwner_value:String, val paket_value:String, val gramme:Boolean, val piece:Boolean, val small:Boolean) : CatalogStateEvent()

    data class AddCatalogOrderItem(val product_item_id:String, val quantity:Int, val unit:String, val size:String, val sortValue:String):CatalogStateEvent()

    class None : CatalogStateEvent()

}
